FROM node:20-slim
 
# Chromium + fuentes y librerías que puppeteer necesita en headless
RUN apt-get update && apt-get install -y --no-install-recommends \
    chromium \
    fonts-liberation \
    libnss3 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libxss1 \
    libasound2 \
    libgbm1 \
    ca-certificates \
 && rm -rf /var/lib/apt/lists/*
 
# index.js ya busca /usr/bin/chromium; lo fijamos explícitamente
ENV CHROME_PATH=/usr/bin/chromium
# Evita que npm descargue otro Chromium (ahorra ~300MB y tiempo de build)
ENV PUPPETEER_SKIP_DOWNLOAD=true
ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
 
WORKDIR /app
COPY package*.json ./
RUN npm ci --omit=dev
COPY index.js ./
RUN mkdir -p .wwebjs_auth
 
EXPOSE 3001
CMD ["node", "index.js"]
 