import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from './components/LoginPage.vue';
import HomePage from './components/HomePage.vue';
import RegistrationPage from "./components/RegistrationPage.vue";
import UserPage from "./components/UserPage.vue";
import SettingsPage from "@/components/SettingsPage.vue";

const routes = [
    { path: '/', component: LoginPage },
    { path: '/home', component: HomePage },
    {path:'/register', component: RegistrationPage},
    {path:'/user' ,component: UserPage},
    {path:'/settings', component: SettingsPage}
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
