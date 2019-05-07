import config from '../../config/config.json';

const BASE = config.baseUrl;

class Routes {
    static DEFAULT = `${BASE}/`;

    static CALCULATE_CREDIT = `${BASE}/calculatecredit`;

    static SIGN_IN = `${BASE}/signin`;

    static SIGN_UP = `${BASE}/signup`;

    static MY_CREDITS = `${BASE}/mycredits`;
}

export default Routes;
