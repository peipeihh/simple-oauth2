# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.pphh.demo</groupId>
    <artifactId>oauth-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.pphh.demo:oauth-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.Oauth2EndpointApi;

import java.io.File;
import java.util.*;

public class Oauth2EndpointApiExample {

    public static void main(String[] args) {
        
        Oauth2EndpointApi apiInstance = new Oauth2EndpointApi();
        ClientVO clientVO = new ClientVO(); // ClientVO | clientVO
        try {
            AuthCodeVO result = apiInstance.authorizeUsingPOST(clientVO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling Oauth2EndpointApi#authorizeUsingPOST");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://localhost:8090*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*Oauth2EndpointApi* | [**authorizeUsingPOST**](docs/Oauth2EndpointApi.md#authorizeUsingPOST) | **POST** /oauth2/authorize | OAuth2授权点
*Oauth2EndpointApi* | [**introspectTokenUsingPOST**](docs/Oauth2EndpointApi.md#introspectTokenUsingPOST) | **POST** /oauth2/introspect | OAuth2令牌检查点
*Oauth2EndpointApi* | [**issueTokenUsingPOST1**](docs/Oauth2EndpointApi.md#issueTokenUsingPOST) | **POST** /oauth2/token | OAuth2令牌颁发点
*Oauth2EndpointApi* | [**revokeTokenUsingPOST**](docs/Oauth2EndpointApi.md#revokeTokenUsingPOST) | **POST** /oauth2/revoke | OAuth2令牌吊销点


## Documentation for Models

 - [AuthCodeVO](docs/AuthCodeVO.md)
 - [ClientVO](docs/ClientVO.md)
 - [OAuth2AccessToken](docs/OAuth2AccessToken.md)
 - [OAuth2RefreshToken](docs/OAuth2RefreshToken.md)
 - [ValidityVO](docs/ValidityVO.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



