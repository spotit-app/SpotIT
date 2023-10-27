FROM node:20.8.0-alpine3.18 AS builder

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
