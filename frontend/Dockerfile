FROM node:20.8.0-alpine3.18 AS builder

ARG VITE_AUTH0_DOMAIN
ARG VITE_AUTH0_CLIENT_ID

ENV VITE_AUTH0_DOMAIN ${VITE_AUTH0_DOMAIN}
ENV VITE_AUTH0_CLIENT_ID ${VITE_AUTH0_CLIENT_ID}

WORKDIR /app
RUN npm install -g pnpm
COPY package.json ./
RUN pnpm i
COPY . .
RUN pnpm build

FROM nginx:1.25.2-alpine3.18-slim
COPY nginx.conf /etc/nginx/templates/default.conf.template
COPY --from=builder /app/dist/ /var/www/SpotIT/html/
EXPOSE 80
