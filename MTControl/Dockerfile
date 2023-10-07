FROM node:20-bookworm

WORKDIR /src

COPY package*.json ./
RUN npm install

STOPSIGNAL SIGKILL

COPY . .
COPY .env .

ENTRYPOINT [ "./start.sh" ]
