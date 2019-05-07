const API = '/api';
const USERS = `${API}/users`;
const CREDITS = `${API}/credits`;

const ApiRoutes = {
    LOGIN: '/login',
    REGISTRATION: `${USERS}/registration`,
    CREDITS,
    CALCULATE_CREDIT: `${CREDITS}/calculate`,
    MY_CREDITS: `${CREDITS}/mycredits`
};

export default ApiRoutes;
