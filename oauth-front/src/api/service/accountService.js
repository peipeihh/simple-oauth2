import restApi from '../restApi'

export default {

    doLogin(request = {}){
        let url = '/api/account/login';
        return restApi.doPostRequest(url, request.token);
    },
    registerAccount(request = {}){
        let url = "api/account/register?email=" + request.usermail;
        return restApi.doPostRequest(url);
    },
    fetchMyAccount(request = {}){
        let url = "api/account/refreshPassword?email=" + request.usermail;
        return restApi.doPostRequest(url);
    },
    saveAccountUserName(request = {}){
        let url = "api/account/setUserName?email=" + request.usermail + "&username=" + request.username;
        return restApi.doPostRequest(url);
    },
    saveAccountPassword(request = {}){
        let url = "api/account/setPassword";
        return restApi.doPostRequest(url, request.token);
    }

}
