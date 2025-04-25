const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
    transpileDependencies: true,

    devServer: {
        port: 3000, // Der Port wird hier auf 3000 gesetzt
        proxy: {
            '/api': {
                target: "https://5ade-2a01-41e3-2320-2500-41b4-734d-a746-9d87.ngrok-free.app",
                changeOrigin: true,
                secure: false,  // Wenn du HTTPS verwendest, setze dies auf 'true'
                pathRewrite: {
                    '^/api': ''  // Entfernt '/api' aus der URL, bevor sie an den Proxy gesendet wird
                }
            }
        }
    },

    pluginOptions: {
        vuetify: {
            // Vuetify-Loader-Konfiguration
        }
    }
})
