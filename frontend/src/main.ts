import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import { aliases, mdi } from 'vuetify/iconsets/mdi'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

const vuetify = createVuetify({
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi
    }
  },
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        dark: false,
        colors: {
          primary: '#2196F3', // blue
          secondary: '#424242', // grey
          accent: '#82B1FF', // light blue
          error: '#FF5252', // red
          info: '#2196F3', // blue
          success: '#4CAF50', // green
          warning: '#FFC107', // yellow
          background: '#f5f5f5' // grey lighten-3
        }
      }
    }
  }
})

app.use(vuetify)
app.mount('#app')
