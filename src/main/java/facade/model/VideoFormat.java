package facade.model;

public enum VideoFormat {
    MP4("mp4"),
    MOV("mov"),
    AVI("avi"),
    WEBM("webm");

    private final String extension;

    VideoFormat(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
