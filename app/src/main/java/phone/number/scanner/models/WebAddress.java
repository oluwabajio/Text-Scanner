package phone.number.scanner.models;

public class WebAddress {

    String name;
    String webUrl;

    public WebAddress(String name, String webUrl) {
        this.name = name;
        this.webUrl = webUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
