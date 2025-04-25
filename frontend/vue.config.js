const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,

    devServer: {
        port: 3000, // Der Port wird hier auf 3000 gesetzt
        proxy: "https://5ade-2a01-41e3-2320-2500-41b4-734d-a746-9d87.ngrok-free.app ",
        //proxy: "http://localhost:8080",
    },

    pluginOptions: {
        vuetify: {
            // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
        }
    }
})
