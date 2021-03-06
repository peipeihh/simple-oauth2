/*
 * 统一认证和授权系统
 * 更多信息请联系pphh
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.pphh.oauth.client.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import org.threeten.bp.OffsetDateTime;

/**
 * AuthCodeVO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-25T13:38:10.202Z")
public class AuthCodeVO {
  @SerializedName("clientName")
  private String clientName = null;

  @SerializedName("code")
  private String code = null;

  @SerializedName("expiration")
  private OffsetDateTime expiration = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("insertTime")
  private OffsetDateTime insertTime = null;

  @SerializedName("redirectUrl")
  private String redirectUrl = null;

  @SerializedName("userName")
  private String userName = null;

  public AuthCodeVO clientName(String clientName) {
    this.clientName = clientName;
    return this;
  }

   /**
   * Get clientName
   * @return clientName
  **/
  @ApiModelProperty(value = "")
  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public AuthCodeVO code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Get code
   * @return code
  **/
  @ApiModelProperty(value = "")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public AuthCodeVO expiration(OffsetDateTime expiration) {
    this.expiration = expiration;
    return this;
  }

   /**
   * Get expiration
   * @return expiration
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getExpiration() {
    return expiration;
  }

  public void setExpiration(OffsetDateTime expiration) {
    this.expiration = expiration;
  }

  public AuthCodeVO id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AuthCodeVO insertTime(OffsetDateTime insertTime) {
    this.insertTime = insertTime;
    return this;
  }

   /**
   * Get insertTime
   * @return insertTime
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getInsertTime() {
    return insertTime;
  }

  public void setInsertTime(OffsetDateTime insertTime) {
    this.insertTime = insertTime;
  }

  public AuthCodeVO redirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
    return this;
  }

   /**
   * Get redirectUrl
   * @return redirectUrl
  **/
  @ApiModelProperty(value = "")
  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public AuthCodeVO userName(String userName) {
    this.userName = userName;
    return this;
  }

   /**
   * Get userName
   * @return userName
  **/
  @ApiModelProperty(value = "")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthCodeVO authCodeVO = (AuthCodeVO) o;
    return Objects.equals(this.clientName, authCodeVO.clientName) &&
        Objects.equals(this.code, authCodeVO.code) &&
        Objects.equals(this.expiration, authCodeVO.expiration) &&
        Objects.equals(this.id, authCodeVO.id) &&
        Objects.equals(this.insertTime, authCodeVO.insertTime) &&
        Objects.equals(this.redirectUrl, authCodeVO.redirectUrl) &&
        Objects.equals(this.userName, authCodeVO.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientName, code, expiration, id, insertTime, redirectUrl, userName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthCodeVO {\n");
    
    sb.append("    clientName: ").append(toIndentedString(clientName)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    insertTime: ").append(toIndentedString(insertTime)).append("\n");
    sb.append("    redirectUrl: ").append(toIndentedString(redirectUrl)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

