

/**
 * Licensed Materials - Property of IBM
 * IBM Cognos Products: BI Glass
 * (C) Copyright IBM Corp. 2015, 2021
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define(['react', 'react-dom', 'ba-ui-carbon', 'baglass/app/ContentView', 'jquery', 'lodash', 'toastr', 'baglass/nls/StringResources', 'baglass/core-client/js/core-client/utils/Utils', 'baglass/utils/ThemeUtils', 'baglass/utils/Utils', '../ajax/CASimpleAuthentication', './Login', './config'], function (React, ReactDOM, Carbon, ContentView, $, _, toastr, StringResources, Utils, ThemeUtils, GlassUtils, CAAuthentication, Login, config) {
  const {
    Loading
  } = Carbon;
  /**
   * This Class is a utility class to provide RESTFul api for glass
   */

  const LoginView = ContentView.extend({
    anonymousAllowed: false,
    oauthReturnValue: '',
    _options() {
      const currentYear = new Date().getFullYear();
      return {
        buttonText: StringResources.get('signInButtonText'),
        anonymousText: StringResources.get('connectAnonymously'),
        anonymousAllowed: this.anonymousAllowed,
        backgroundImage: 'images/login.png',
        brandingText: '',
        loginLegalText: StringResources.get('loginLegalText', {
          'fullYear': currentYear
        }),
        nameSpaceLabel: StringResources.get('selectNamespace'),
        newPasswordsDoNotMatch: StringResources.get('newPasswordsDoNotMatch'),
        changePassword: StringResources.get('changePassword')
      };
    },

    /**
     * constructor
     * @options {object}
     */
    init(options, appView) {
      //check if return from OAuth, get OAuthTokenName parameter from Url(OAuth token)
      this.oauthReturnValue = this.getQueryVariable(config['OAuthTokenName'])

      if (!this.oauthReturnValue) {
        //go to OAuth 
        this._tryGoToOAuth();
      } else {
        LoginView.inherited('init', this, arguments);
        this.appView = appView;
        $.extend(this, options);
      }
    },
    _tryGoToOAuth() {
      //get related parameter and store in cookie before go to OAuth page
      var ps = this.getQueryVariable('ps');
      var ccmd = this.getQueryVariable('c_cmd');
      var _server = this.getQueryVariable('server');
      var _host = this.getQueryVariable('host');
      var pg = this.getQueryVariable('pg');
      var nocache = this.getQueryVariable('tm1web.nocache');
      var oauthUrl = config['OAuthUrl'];
      config['namespace'] = this.getQueryVariable('namespace') || config['namespace'];

      if (ps) {
        document.cookie = 'ca_auth_ps=' + ps;
      } else {
        document.cookie = "ca_auth_ps=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      }
      if (ccmd) {
        document.cookie = 'ca_auth_ccmd=' + ccmd;
      } else {
        document.cookie = "ca_auth_ccmd=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      }
      if (_server) {
        document.cookie = 'ca_auth_server=' + _server;
      } else {
        document.cookie = "ca_auth_server=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      }
      if (_host) {
        document.cookie = 'ca_auth_host=' + _host;
      } else {
        document.cookie = "ca_auth_host=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      }
      if (pg) {
        document.cookie = 'ca_auth_pg=' + pg;
      } else {
        document.cookie = "ca_auth_pg=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      }
      if (nocache) {
        document.cookie = 'ca_auth_nocache=' + nocache;
      } else {
        document.cookie = "ca_auth_nocache=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      }
      location.href = oauthUrl;
    },
    _tryAuthFinishHandle() {
      //OAuth authentication success, jump to application with stored parameters
      function getCookieVal(offset) {
        var endstr = document.cookie.indexOf(";", offset);
        if (endstr == -1)
          endstr = document.cookie.length;
        return document.cookie.substring(offset, endstr);
      }

      function getCookie(name) {
        var arg = name + "=";
        var alen = arg.length;
        var clen = document.cookie.length;
        var i = 0;
        while (i < clen) {
          var j = i + alen;
          if (document.cookie.substring(i, j) == arg)
            return getCookieVal(j);
          i = document.cookie.indexOf(" ", i) + 1;
          if (i == 0)
            break;
        }
        return null;
      }

      var vPs = getCookie('ca_auth_ps');
      var vCCmd = getCookie('ca_auth_ccmd');
      var vServer = getCookie('ca_auth_server');
      var vHost = getCookie('ca_auth_host');
      var vPg = getCookie('ca_auth_pg');
      var vNocache = getCookie('ca_auth_nocache');
      var cam_passport = getCookie('cam_passport');
      console.log(vPs, vCCmd, vServer, vHost);
      document.cookie = "ca_auth_ps=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      document.cookie = "ca_auth_ccmd=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      document.cookie = "ca_auth_server=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"
      document.cookie = "ca_auth_host=; expires=Thu, 01 Jan 1970 00:00:00 UTC;"

      var url;
      if (vCCmd) {
        url = decodeURIComponent(vCCmd);
        if (url.indexOf('?') >= 0) {
          url = url + '&cam_passport=' + cam_passport;
        } else {
          url = url + '?cam_passport=' + cam_passport;
        }
      }

      if (vPg && vPg == 'CamPassportServlet') {
        url = url + '&server=' + vServer + '&ps=' + vPs + '&host=' + vHost + '&pg=' + vPg + '&tm1web.nocache=' + vNocache;
        window.location.href = url
      } else if (vPs) {
        if (cam_passport) {
          url = url + '&server=' + vServer + '&ps=' + vPs + '&pg=applications.jsp&host=' + vHost;
          window.location.href = url
        }
      } else if (vCCmd) {
        if (cam_passport) {
          window.location.href = url;
        }
      } else {
        $(Utils.getCurrentWindow()).trigger('ca.loginSuccessful');
      }
    },

    remove() { },

    _tryDirectLogin() {
      if (GlassUtils.isUiPreview()) {
        return Promise.reject(this._generatePreviewPromptInfo());
      } else {
        return Promise.resolve(this._trySubmitCredentials());
      }
    },

    _generatePreviewPromptInfo() {
      return {
        displayObjects: [{
          'name': 'CAMUsername',
          'caption': StringResources.get('userPlaceholder') + ':',
          'type': 'text'
        }, {
          'name': 'CAMPassword',
          'caption': StringResources.get('passPlaceholder') + ':',
          'type': 'textnoecho'
        }]
      };
    },

    _trySubmitCredentials(loginPrompts) {
      //pass oauthReturnValue(OAuth token) to Provider as username parameter
      const origin = $.extend({}, this.origin);
      origin.requestMethod = 'POST';

      if (this.oauthReturnValue) {
        var loginPrompts = {
          parameters: [{
            name: 'h_CAM_action',
            value: 'logonAs'
          }, {
            name: 'CAMUsername',
            value: this.oauthReturnValue
          }, {
            name: 'CAMPassword',
            value: ''
          }, {
            name: 'CAMNamespace',
            value: config['namespace']
          }]
        };
      } else if (!loginPrompts) {
        var loginPrompts = {
          parameters: [{
            name: 'h_CAM_action',
            value: 'logonAs'
          }]
        }
        let queryString = '';

        if (this.origin) {
          queryString = this.origin.queryString;
        }

        loginPrompts.parameters = GlassUtils.getFilteredLoginParameters(this.glassContext, queryString, loginPrompts.parameters);

        if (loginPrompts.parameters.length === 1 && (!this.origin || this.origin.legacyLogin !== true && this.origin.secondaryLogin !== true)) {
          loginPrompts = undefined;
        }
      }
      
      return this._getCAAuthentication().submitCredentials(this.glassContext, loginPrompts, origin);
    },

    render() {
      this.$el.append('<div class="loginOverlay"><div class="loginWorking"></div></div><div class="loginView" id="content"/>');

      if (this.appView && this.appView.content && this.appView.content.origin && this.appView.content.origin.authError && this.appView.content.origin.authError.displayObjects) {
        return this._renderPrompt(this.appView.content.origin.authError).then(element => {
          ReactDOM.render(element, this.$el.find('#content')[0]);
          return this;
        });
      } else {
        return this._tryDirectLogin().then(response => {
          const authInfo = response.data;
          this._tryAuthFinishHandle();
        }).catch(error => {
          return this._renderPrompt(error).then(element => {
            ReactDOM.render(element, this.$el.find('#content')[0]);
            return this;
          });
        });
      }
    },

    _renderPrompt(error) {
      return ThemeUtils.getCurrentThemeValues(this.glassContext).then(response => {
        this._clearToasts();

        this.hiddenLoginPrompts = {
          'parameters': []
        };
        this.anonymousAllowed = this.glassContext.authInfo && this.glassContext.authInfo.isAnonymous === true;

        const renderOptions = this._options();

        renderOptions.brandingText = _.escape(response['brandText']);
        renderOptions.loginBrandMessage = StringResources.get('loginBrandMessage', {
          brandingText: renderOptions.brandingText
        });
        this._brandingText = renderOptions.brandingText;
        Utils.closeDialog();
        this.props = {
          renderOptions: renderOptions,
          displayObjects: error.displayObjects,
          message: error.message,
          onSubmit: this.submit.bind(this)
        };
        return /*#__PURE__*/React.createElement(Login, this.props);
      });
    },

    _getCAAuthentication() {
      return new CAAuthentication();
    },

    submit(loginPrompts) {
      const connectAnonymously = loginPrompts && loginPrompts.stayAnonymous === true;

      if (connectAnonymously) {
        $(Utils.getCurrentWindow()).trigger('ca.loginSuccessful', {
          stayAnonymous: true
        });
        return Promise.resolve();
      } else {
        const parms = loginPrompts && loginPrompts.parameters;
        const isNameSpace = parms && parms.length === 1 && parms[0].name === 'CAMNamespace';

        this._showProgress(isNameSpace ? undefined : StringResources.get('loginInProgress'));

        return this._trySubmitCredentials(loginPrompts).then(authInfo => {
          Utils.activateAriaAlert(StringResources.get('loginSuccess'));
          $(Utils.getCurrentWindow()).trigger('ca.loginSuccessful', authInfo);
        }).catch(error => {
          this._hideProgress();

          if (error.isExternalLogin) {
            const originInfo = this.origin ? this.origin : {};
            return this._getCAAuthentication().externalLogin(this.glassContext, error, originInfo).then(authInfo => {
              Utils.activateAriaAlert(StringResources.get('loginSuccess'));
              return this._getCAAuthentication()._processSuccessfulLogin(this.glassContext, authInfo, originInfo);
            }).then(authInfo => {
              $(Utils.getCurrentWindow()).trigger('ca.loginSuccessful', authInfo);
            });
          } else {
            // throw error;
            console.log(error)
          }
        });
      }
    },

    _hideProgress() {
      this.$el.find('.loginOverlay').hide();
      const container = this.$el.find('.loginWorking');
      ReactDOM.unmountComponentAtNode(container[0]);
    },

    _showProgress(message) {
      const container = this.$el.find('.loginWorking');
      ReactDOM.render( /*#__PURE__*/React.createElement(Loading, null), container[0]);
      container.removeAttr('role');
      this.$el.find('.loginOverlay').show();

      if (message) {
        Utils.activateAriaAlert(message);
      }
    },

    _clearToasts() {
      toastr.options.hideDuration = 1;
      toastr.clear();
    },

    getTitle() {
      return this._brandingText || '';
    },

    setFocus() {
      if (document.activeElement) {
        // drop focus if it's held elsewhere...
        document.activeElement.blur();
      }

      const $userPromptInputs = this.$el.find('#content .userPromptInput');

      if ($userPromptInputs.length > 0) {
        $userPromptInputs[0].focus();
      } else {
        const $firstTabbable = this.$el.find(':tabbable:first');

        if ($firstTabbable.length > 0) {
          $firstTabbable.focus();
        }
      }
    },

    getQueryVariable(variable, query) {
      query = query || window.location.search.substring(1)
      query = decodeURIComponent(query);
      var vars = query.split("&");
      for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {

          return pair[1];
        }
      }

      return "";
    },

    hexToString(hex) {
      var tmp = '';
      if (hex.length % 2 == 0) {
        for (var i = 0; i < hex.length; i += 2) {
          tmp += '%' + hex.charAt(i) + hex.charAt(i + 1);
        }
      }
      return decodeURIComponent(tmp);
    }

  });
  return LoginView;
});
//# sourceMappingURL=LoginView.js.map
