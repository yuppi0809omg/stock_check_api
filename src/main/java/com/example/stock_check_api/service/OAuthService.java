//package com.example.stock_check_api.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.social.github.api.GitHubUserProfile;
//import org.springframework.social.github.api.impl.GitHubTemplate;
//import org.springframework.social.github.connect.GitHubConnectionFactory;
//import org.springframework.social.oauth2.AccessGrant;
//import org.springframework.social.oauth2.GrantType;
//import org.springframework.social.oauth2.OAuth2Operations;
//import org.springframework.social.oauth2.OAuth2Parameters;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OAuthService {
//    // application.propertiesからgithub client idとclient secretとcallbak urlを読み込む
//
//    @Value("${github.client-id}")
//    private String clientId;
//
//    @Value("${github.client-secret}")
//    private String clientSecret;
//
//    @Value("${github.callbackurl}")
//    private String callbackUrl;
//
//    private OAuth2Operations operations() {
//        GitHubConnectionFactory gitHubConnectionFactory = new GitHubConnectionFactory(clientId, clientSecret);
//        OAuth2Operations operations = gitHubConnectionFactory.getOAuthOperations();
//        return operations;
//    }
//
//
//    /**
//     * 認証URLを取得する
//     *
//     * @return 取得した認証URL
//     **/
//    public String getAuthorizationUrl() {
//        return operations().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, new OAuth2Parameters());
//
//    }
//
//    /**
//     * アクセストークンを取得
//     *
//     * @param code 認可後サーバーから発行される認可コード
//     * @return サーバーから発行されるアクセストークン
//     */
//    public String retrieveAccessToken(String code) {
//        AccessGrant accessGrant = operations().exchangeForAccess(code, callbackUrl, null);
//        return accessGrant.getAccessToken();
//    }
//
//    /**
//     * ユーザー情報取得
//     *
//     * @param accessToken サーバーから取得したアクセストークン
//     * @return アクセストークンと交換で取得したユーザー情報
//     */
//    public GitHubUserProfile getUserProfile(String accessToken) {
//        GitHubTemplate gitHubTemplate = new GitHubTemplate(accessToken);
//        return gitHubTemplate.userOperations().getUserProfile();
//    }
//
//}
