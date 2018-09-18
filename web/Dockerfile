FROM python:3.7

COPY . /usr/src/app
WORKDIR /usr/src/app

EXPOSE 80
RUN pip install -r requirements.txt

ENTRYPOINT ["python", "web_bot.py"]
