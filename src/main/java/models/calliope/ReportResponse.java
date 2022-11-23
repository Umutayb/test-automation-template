package models.calliope;

public class ReportResponse {
    Integer report_id;
    String report_url;
    String report_status_url;
    String report_import_status;
    String report_import_progress;
    String message;

    public Integer report_id() {return report_id;}

    public String report_url() {return report_url;}

    public String report_status_url() {return report_status_url;}

    public String report_import_status() {return report_import_status;}

    public String report_import_progress() {return report_import_progress;}

    public String message() {return message;}
}
