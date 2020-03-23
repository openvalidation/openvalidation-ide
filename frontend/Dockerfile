FROM node:13.8.0-alpine as build
LABEL description="build container for openvalidation-ide-frontend"
ENV PATH build/node_modules/.bin:$PATH

COPY package.json package-lock.json /build/
WORKDIR /build
RUN npm ci
RUN npm install -g @angular/cli@9.0.7

COPY angular.json browserslist tsconfig* /build/
COPY src /build/src
RUN ng build --prod


FROM nginx:1.17.8-alpine as runtime
LABEL description="openvalidation-ide-frontend"
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /build/dist/openvalidation-ide-frontend /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]
