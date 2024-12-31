window.queryGlobalStyle({
    request: 'queryGlobalStyle', onSuccess: function (response) {
        let result = JSON.parse(response);
        let theme = result.globalColorStyle === 'dark' ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', theme);
    }, onFailure: function (error_code, error_message) {
    }
});
