from flask import render_template, session, flash, redirect, url_for, abort, request, jsonify
from src import app
from functools import partial
import pprint
import logging
from requests_oauthlib import OAuth2Session
import os
import toml
import redis

pp = pprint.PrettyPrinter(indent=4)

r = redis.Redis(host='localhost', port=6379, db=0)

gunicorn_logger = logging.getLogger('gunicorn.error')
app.logger.handlers = gunicorn_logger.handlers

info = toml.load("./docker/Config-Web.toml")

CLIENT_ID = info["CLIENT_ID"]
CLIENT_SECRET = info["CLIENT_SECRET"]
REDIRECT_URI = info["REDIRECT_URI"]

API_BASE_URL = 'https://discordapp.com/api'
AUTHORIZATION_BASE_URL = API_BASE_URL + '/oauth2/authorize'
TOKEN_URL = API_BASE_URL + '/oauth2/token'

if 'http://' in REDIRECT_URI:
    os.environ['OAUTHLIB_INSECURE_TRANSPORT'] = 'true'

app.logger.setLevel(logging.INFO)


def has_perm(perm_int: int, perm: int):
    """
    Returns whether user has specified permission
    :param perm_int: permission int for the user (potentially different for each server)
    :param perm: permission that we want to check for
    :return: bool
    """
    return perm_int & perm


def multi_perm(perm_int: int, wanted: list, op):
    """
    Checks for multiple permissions
    :param perm_int: permission int for the user (potentially different for each server)
    :param wanted: list of permissions (type: int) to check for
    :param op: function to feed the list of bools, one for each permission, through (`any` or `all` for example)
    :return: bool
    """
    return op(has_perm(perm_int, perm) for perm in wanted)


perm_any = partial(multi_perm, op=any)
perm_all = partial(multi_perm, op=all)


def token_updater(token):
    session['oauth2_token'] = token


def make_session(token=None, state=None, scope=None):
    return OAuth2Session(
        client_id=CLIENT_ID,
        token=token,
        state=state,
        scope=scope,
        redirect_uri=REDIRECT_URI,
        auto_refresh_kwargs={
            'client_id': CLIENT_ID,
            'client_secret': CLIENT_SECRET,
        },
        auto_refresh_url=TOKEN_URL,
        token_updater=token_updater)


@app.route('/')
@app.route('/index')
def index():
    if "username" in session:
        username = session["username"] + "#" + session["discriminator"]
        email = session["email"]
        discord_id = session["id"]
        login_status = f"Logged in as {username}! Your data is: email: {email}, id: {discord_id}"
    else:
        login_status = "Not logged in!"
    return render_template('index.html', title='Home', login_status=login_status)


@app.route('/discord')
def discord_login():
    scope = request.args.get(
        'scope',
        'identify email')
    discord = make_session(scope=scope.split(' '))
    authorization_url, state = discord.authorization_url(AUTHORIZATION_BASE_URL)
    session['oauth2_state'] = state
    return redirect(authorization_url)


@app.route('/discord_callback', methods=['GET', 'POST'])
def discord_callback():
    if request.values.get('error'):
        return request.values['error']
    discord = make_session(state=session.get('oauth2_state'))
    token = discord.fetch_token(
        TOKEN_URL,
        client_secret=CLIENT_SECRET,
        authorization_response=request.url)
    session['oauth2_token'] = token
    discord = make_session(token=session.get('oauth2_token'))
    user = discord.get(API_BASE_URL + '/users/@me').json()
    session["username"] = user["username"]
    session["discriminator"] = user["discriminator"]
    session["id"] = user["id"]
    session["email"] = user["email"]
    return redirect(url_for('index'))


@app.route('/me')
def me():
    discord = make_session(token=session.get('oauth2_token'))
    user = discord.get(API_BASE_URL + '/users/@me').json()
    guilds = discord.get(API_BASE_URL + '/users/@me/guilds').json()
    # connections = discord.get(API_BASE_URL + '/users/@me/connections').json()
    out = f"""{pprint.pformat(user, indent=4)}\n{pprint.pformat(guilds, indent=4)}"""
    # out = f"""<pre>{pprint.pformat(user, indent=4)}\n{pprint.pformat(guilds, indent=4)}</pre>"""
    return render_template("blank.html", arg=out)


@app.route("/view_db")
def view_db():
    if "username" not in session:
        flash("You are not logged in!")
        return redirect(url_for("index"))
    return render_template("view_database.html", title="Database", db=r)


@app.route('/logout')
def discord_logout():
    session.pop('username', None)
    session.pop('email', None)
    session.pop('id', None)
    session.pop('discriminator', None)
    flash("Successfully logged out!")
    return redirect(url_for('index'))


@app.route('/test')
def hello_world():
    return 'Hello world!'
