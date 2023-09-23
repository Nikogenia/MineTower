FROM python:3.11-bookworm

WORKDIR /src

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

STOPSIGNAL SIGINT

COPY . .

CMD [ "./start.sh" ]
