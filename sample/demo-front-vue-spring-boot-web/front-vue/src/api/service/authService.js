import restApi from '../restApi'

export default {
    fetchToken(request = {}) {
        let url = 'api/oauth2/accessToken?code=' + request.code;
        return restApi.doGetRequest(url);
    },
    refreshToken(request = {}) {
        let url = 'api/oauth2/refreshToken?refresh_token=' + request.refresh_token;
        return restApi.doGetRequest(url);
    },
    revokeToken(request = {}) {
        let url = 'api/oauth2/revokeToken?token=' + request.token;
        return restApi.doGetRequest(url);
    },
    fetchLoginUrl(request = {}) {
        let url = null;
        if (request != null && request.callback != null) {
            url = 'api/oauth2/redirectUrl?callback=' + encodeURIComponent(request.callback);
        } else {
            url = 'api/oauth2/redirectUrl';
        }
        return restApi.doGetRequest(url);
    },
    fetchTestData(request = {}) {
        let url = 'api/test/fetch';
        return restApi.doGetRequest(url);
    },
    updateTestData(request = {}) {
        let url = 'api/test/update?newData=' + request.newData;
        return restApi.doPostRequest(url);
    }
}
