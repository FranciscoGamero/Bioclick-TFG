FROM node:18-alpine AS builder
WORKDIR /web
COPY package*.json ./
RUN npm install -g @angular/cli
RUN npm install
COPY . .
RUN ng build Bioclick --configuration production --aot

FROM nginx:alpine
WORKDIR /web
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /web/dist/bioclick/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]