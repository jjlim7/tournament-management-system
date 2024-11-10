import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.min.css'
//customising bootstrap default colour
import './assets/custom.scss'

import AOS from 'aos'
import 'aos/dist/aos.css'

//import sweet alert
import VueSweetalert2 from 'vue-sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
const SwalOptions = {
    confirmButtonColor: '#FA9021',
    cancelButtonColor: '#DDDDDD',
};

//import cookie
import VueCookies from 'vue-cookies'

//import primevue and configure it
import PrimeVue from 'primevue/config';
import Aura from '@primevue/themes/aura';

// Import Font Awesome
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons'; // Use solid icons


library.add(fas);

//initialise app
const app = createApp(App)
AOS.init()
app.component('font-awesome-icon', FontAwesomeIcon);
app.use(PrimeVue, {
    theme: {
        preset: Aura
    },
    zIndex: {
        modal: 1100,        //dialog, drawer
        overlay: 1100,      //select, popover
        menu: 1100,         //overlay menus
        tooltip: 1100      //tooltip
    }
});
app.use(createPinia())
app.use(router)
app.use(VueCookies, { expires: '7d'})
app.use(VueSweetalert2,SwalOptions);
app.mount('#app')
