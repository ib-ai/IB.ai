from flask import Flask
from .config import Config

app = Flask(__name__, static_url_path="/static")

app.debug = True

app.secret_key = b"\xc3fyc\xe1\xd5'\xf5\xfc\xd6\xfa\xb3\xd7*\x89\xe4"  # Keep this secret
app.config.from_object(Config)

from src import routes
