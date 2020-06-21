import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '../views/Home.vue';
import SurveyEdit from '../views/SurveyEdit.vue';
import SurveyView from '../views/SurveyView.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
const jwt = require('jsonwebtoken');

Vue.use(VueRouter);

const routes: RouteConfig[] = [
  {
    path: '/home',
    name: 'Home',
    component: Home,
  },
  {
    path: '/survey/edit/:qnId?',
    name: 'SurveyEdit',
    component: SurveyEdit,
  },
  {
    path: '/survey/view/:qnId?',
    name: 'SurveyView',
    component: SurveyView,
  },
  {
    path: '/reply/:replyId?',
    name: 'SurveyView',
    component: SurveyView,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '*',
    name: 'Wildcard',
    component: Home,
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {

  if (to.path.startsWith('/register') ||to.path.startsWith('/login') || to.path.startsWith('/survey/view')) {
    next();

  } else {
    let userName:string = localStorage.getItem('userName');
    let token:string = localStorage.getItem('token');
    if (!userName || !token) {
      next({
        path: '/login',
      });

    } else {

      next();

    }

  }

})
export default router;
