package movie_app_system.demo.dto;

public class GoogleLoginRequest {
    private String idToken;

    // Constructors
    public GoogleLoginRequest() {}
    public GoogleLoginRequest(String idToken) { this.idToken = idToken; }

    // Getter và Setter
    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
}