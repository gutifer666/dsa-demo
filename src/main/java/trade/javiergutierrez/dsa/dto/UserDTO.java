package trade.javiergutierrez.dsa.dto;

import trade.javiergutierrez.dsa._shared.annotations.IgnoreInComparison;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Integer age;
    private String additionalField;
    @IgnoreInComparison
    private String extraProperty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getExtraProperty() {
        return extraProperty;
    }

    public void setExtraProperty(String extraProperty) {
        this.extraProperty = extraProperty;
    }

    public String getAdditionalField() {
        return additionalField;
    }

    public void setAdditionalField(String additional) {
        this.additionalField = additional;
    }
}
