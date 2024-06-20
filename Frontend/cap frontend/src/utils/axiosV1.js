import axios from 'axios';
import config from './config';

const axiosV1 = axios.create({
    baseURL: `${config.API_HOST}/api`,
});

export default axiosV1;
