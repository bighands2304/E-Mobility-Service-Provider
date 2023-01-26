FROM node:14
RUN mkdir -p /usr/src/app/node_modules && chown -R node:node /usr/src/app
WORKDIR /usr/src/app
COPY package*.json ./
USER node
RUN npm ci
COPY . .
EXPOSE 8080
CMD [ "npm", "start" ]