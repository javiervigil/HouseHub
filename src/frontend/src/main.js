import { createApp } from 'vue'
import { definePreset } from '@primeuix/themes';
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';
import Tooltip from 'primevue/tooltip';
import App from './App.vue'
import router from './router'
import ToastService from 'primevue/toastservice';
import ConfirmationService from 'primevue/confirmationservice';

const MyPreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{sky.50}',
            100: '{sky.100}',
            200: '{sky.200}',
            300: '{sky.300}',
            400: '{sky.400}',
            500: '{sky.800}',
            600: '{sky.700}',
            700: '{sky.700}',
            800: '{sky.800}',
            900: '{sky.900}',
            950: '{sky.950}'
        }
    }
});

const app = createApp(App);

app.directive('tooltip', Tooltip);
app.use(PrimeVue, {
    theme: {
        preset: MyPreset
    },
});

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(ToastService);
app.use(ConfirmationService);
app.use(router).mount('#app');
