from src import app
import toml

info = toml.load("Config-Web.toml")
REDIRECT_URI = info["REDIRECT_URI"]

if __name__ == '__main__':
    app.run(host=REDIRECT_URI, port=80, debug=True)
