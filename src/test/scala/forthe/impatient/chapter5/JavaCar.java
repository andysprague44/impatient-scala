package forthe.impatient.chapter5;

/**
 * A java class of a car for q9
 */
public class JavaCar {
    public String manufacturer;
    public String model;
    public int year;
    public String license;

    public JavaCar(String manufacturer, String model, int year, String license) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.license = license;
    }

    public JavaCar(String manufacturer, String model, int year) {
        this(manufacturer, model, year, "");
    }

    public JavaCar(String manufacturer, String model, String license) {
        this(manufacturer, model, -1, license);
    }

    public JavaCar(String manufacturer, String model) {
        this(manufacturer, model, -1);
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
