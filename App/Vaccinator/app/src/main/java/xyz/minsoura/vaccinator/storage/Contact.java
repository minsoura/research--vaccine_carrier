package xyz.minsoura.vaccinator.storage;

/**
 * Created by min on 2016-03-25.
 */
public class Contact {

    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    String idNumber;

    public String getPosLat() {
        return posLat;
    }

    public void setPosLat(String posLat) {
        this.posLat = posLat;
    }

    public String getPosLong() {
        return posLong;
    }

    public void setPosLong(String posLong) {
        this.posLong = posLong;
    }

    String posLat;
    String posLong;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFirstNameM() {
        return firstNameM;
    }

    public void setFirstNameM(String firstNameM) {
        this.firstNameM = firstNameM;
    }

    public String getMiddleNameM() {
        return middleNameM;
    }

    public void setMiddleNameM(String middleNameM) {
        this.middleNameM = middleNameM;
    }

    public String getLastNameM() {
        return lastNameM;
    }

    public void setLastNameM(String lastNameM) {
        this.lastNameM = lastNameM;
    }

    public String getDialogCountryM() {
        return dialogCountryM;
    }

    public void setDialogCountryM(String dialogCountryM) {
        this.dialogCountryM = dialogCountryM;
    }

    public String getDialogDistrictM() {
        return dialogDistrictM;
    }

    public void setDialogDistrictM(String dialogDistrictM) {
        this.dialogDistrictM = dialogDistrictM;
    }

    public String getAddress1M() {
        return address1M;
    }

    public void setAddress1M(String address1M) {
        this.address1M = address1M;
    }

    public String getAddress2M() {
        return address2M;
    }

    public void setAddress2M(String address2M) {
        this.address2M = address2M;
    }

    public String getPostalCodeM() {
        return postalCodeM;
    }

    public void setPostalCodeM(String postalCodeM) {
        this.postalCodeM = postalCodeM;
    }

    public String getWeightM() {
        return weightM;
    }

    public void setWeightM(String weightM) {
        this.weightM = weightM;
    }

    public String getHeightM() {
        return heightM;
    }

    public void setHeightM(String heightM) {
        this.heightM = heightM;
    }

    String firstNameM;
    String middleNameM;
    String lastNameM;
    String dialogCountryM="";
    String dialogDistrictM="";
    String address1M;
    String address2M;
    String postalCodeM;
    String weightM;
    String heightM;
    //String longitudeM;
    //  String latitudeM;

    String bcgCheck;
    String polioFirstCheck;
    String polioSecondCheck;

    public String getBcgCheck() {
        return bcgCheck;
    }

    public void setBcgCheck(String bcgCheck) {
        this.bcgCheck = bcgCheck;
    }

    public String getPolioFirstCheck() {
        return polioFirstCheck;
    }

    public void setPolioFirstCheck(String polioFirstCheck) {
        this.polioFirstCheck = polioFirstCheck;
    }

    public String getPolioSecondCheck() {
        return polioSecondCheck;
    }

    public void setPolioSecondCheck(String polioSecondCheck) {
        this.polioSecondCheck = polioSecondCheck;
    }

    public String getPolioThirdCheck() {
        return polioThirdCheck;
    }

    public void setPolioThirdCheck(String polioThirdCheck) {
        this.polioThirdCheck = polioThirdCheck;
    }

    public String getDptFirstCheck() {
        return dptFirstCheck;
    }

    public void setDptFirstCheck(String dptFirstCheck) {
        this.dptFirstCheck = dptFirstCheck;
    }

    public String getDptSecondCheck() {
        return dptSecondCheck;
    }

    public void setDptSecondCheck(String dptSecondCheck) {
        this.dptSecondCheck = dptSecondCheck;
    }

    public String getDptThridCheck() {
        return dptThirdCheck;
    }

    public void setDptThirdCheck(String dptThirdCheck) {
        this.dptThirdCheck = dptThirdCheck;
    }

    public String getMeasleCheck() {
        return measleCheck;
    }

    public void setMeasleCheck(String measleCheck) {
        this.measleCheck = measleCheck;
    }

    public String getJeCheck() {
        return jeCheck;
    }

    public void setJeCheck(String jeCheck) {
        this.jeCheck = jeCheck;
    }

    String polioThirdCheck;
    String dptFirstCheck;
    String dptSecondCheck;

    public String getDptThirdCheck() {
        return dptThirdCheck;
    }

    String dptThirdCheck;
    String measleCheck;
    String jeCheck;

    String bcgDate;
    String polioFirstDate;

    public String getBcgDate() {
        return bcgDate;
    }

    public void setBcgDate(String bcgDate) {
        this.bcgDate = bcgDate;
    }

    public String getPolioFirstDate() {
        return polioFirstDate;
    }

    public void setPolioFirstDate(String polioFirstDate) {
        this.polioFirstDate = polioFirstDate;
    }

    public String getPolioSecondDate() {
        return polioSecondDate;
    }

    public void setPolioSecondDate(String polioSecondDate) {
        this.polioSecondDate = polioSecondDate;
    }

    public String getPolioThirdDate() {
        return polioThirdDate;
    }

    public void setPolioThirdDate(String polioThirdDate) {
        this.polioThirdDate = polioThirdDate;
    }

    public String getDptFirstDate() {
        return dptFirstDate;
    }

    public void setDptFirstDate(String dptFirstDate) {
        this.dptFirstDate = dptFirstDate;
    }

    public String getDptSecondDate() {
        return dptSecondDate;
    }

    public void setDptSecondDate(String dptSecondDate) {
        this.dptSecondDate = dptSecondDate;
    }

    public String getDptThirdDate() {
        return dptThirdDate;
    }

    public void setDptThirdDate(String dptThirdDate) {
        this.dptThirdDate = dptThirdDate;
    }

    public String getMeasleDate() {
        return measleDate;
    }

    public void setMeasleDate(String measleDate) {
        this.measleDate = measleDate;
    }

    public String getJeDate() {
        return jeDate;
    }

    public void setJeDate(String jeDate) {
        this.jeDate = jeDate;
    }

    String polioSecondDate;
    String polioThirdDate;
    String dptFirstDate;
    String dptSecondDate;
    String dptThirdDate;
    String measleDate;
    String jeDate;

    public String getFirstRegistration() {
        return firstRegistration;
    }

    public void setFirstRegistration(String firstRegistration) {
        this.firstRegistration = firstRegistration;
    }

    String firstRegistration ="yes";


    public Contact(String idNumber,String bcgCheck, String bcgDate, String polioFirstCheck, String polioFirstDate, String polioSecondCheck, String polioSecondDate, String polioThirdCheck, String polioThirdDate, String dptFirstCheck,String dptFirstDate,String dptSecondCheck, String dptSecondDate, String dptThirdCheck, String dptThirdDate, String measleCheck, String measleDate, String jeCheck, String jeDate){
        this.idNumber = idNumber;
        this.bcgCheck = bcgCheck;
        this.bcgDate = bcgDate;
        this.polioFirstCheck = polioFirstCheck;
        this.polioFirstDate = polioFirstDate;
        this.polioSecondCheck = polioSecondCheck;
        this.polioSecondDate = polioSecondDate;
        this.polioThirdCheck = polioThirdCheck;
        this.polioThirdDate = polioThirdDate;

        this.dptFirstCheck = dptFirstCheck;
        this.dptFirstDate = dptFirstDate;
        this.dptSecondCheck = dptSecondCheck;
        this.dptSecondDate = dptSecondDate;
        this.dptThirdCheck = dptThirdCheck;
        this.dptThirdDate = dptThirdDate;

        this.measleCheck = measleCheck;
        this.measleDate = measleDate;

        this.jeCheck = jeCheck;
        this.jeDate = jeDate;


    }


    public Contact(int id,String idNumber,String bcgCheck, String bcgDate, String polioFirstCheck, String polioFirstDate, String polioSecondCheck, String polioSecondDate, String polioThirdCheck, String polioThirdDate, String dptFirstCheck,String dptFirstDate,String dptSecondCheck, String dptSecondDate, String dptThirdCheck, String dptThirdDate, String measleCheck, String measleDate, String jeCheck, String jeDate){
          this.id = id;
        this.idNumber = idNumber;
        this.bcgCheck = bcgCheck;
        this.bcgDate = bcgDate;
        this.polioFirstCheck = polioFirstCheck;
        this.polioFirstDate = polioFirstDate;
        this.polioSecondCheck = polioSecondCheck;
        this.polioSecondDate = polioSecondDate;
        this.polioThirdCheck = polioThirdCheck;
        this.polioThirdDate = polioThirdDate;

        this.dptFirstCheck = dptFirstCheck;
        this.dptFirstDate = dptFirstDate;
        this.dptSecondCheck = dptSecondCheck;
        this.dptSecondDate = dptSecondDate;
        this.dptThirdCheck = dptThirdCheck;
        this.dptThirdDate = dptThirdDate;

        this.measleCheck = measleCheck;
        this.measleDate = measleDate;

        this.jeCheck = jeCheck;
        this.jeDate = jeDate;


    }

  public  Contact( String idNumber0,String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height){

      this.idNumber = idNumber0;
      this.firstNameM = firstName;
      this.middleNameM = middleName;
      this.lastNameM = lastName;
      this.dialogCountryM = dialogCountry;
      this.dialogDistrictM= dialogDistrict;
      this.address1M  = address1;
      this.address2M = address2;
      this.postalCodeM = postalCode;
      this.weightM = weight;
      this.heightM= height;

    }

    public  Contact( String idNumber0,String posLat, String posLong,String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height){

        this.idNumber = idNumber0;
        this.posLat = posLat;
        this.posLong = posLong;
        this.firstNameM = firstName;
        this.middleNameM = middleName;
        this.lastNameM = lastName;
        this.dialogCountryM = dialogCountry;
        this.dialogDistrictM= dialogDistrict;
        this.address1M  = address1;
        this.address2M = address2;
        this.postalCodeM = postalCode;
        this.weightM = weight;
        this.heightM= height;

    }
    public Contact( int id, String idNumber0,String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height){
        this.id= id;
        this.idNumber = idNumber0;
        this.firstNameM = firstName;
        this.middleNameM = middleName;
        this.lastNameM = lastName;
        this.dialogCountryM = dialogCountry;
        this.dialogDistrictM= dialogDistrict;
        this.address1M  = address1;
        this.address2M = address2;
        this.postalCodeM = postalCode;
        this.weightM = weight;
        this.heightM= height;

    }

    public Contact( int id, String idNumber0,String posLat, String posLong,String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height){
        this.id= id;
        this.idNumber = idNumber0;
        this.posLat = posLat;
        this.posLong = posLong;
        this.firstNameM = firstName;
        this.middleNameM = middleName;
        this.lastNameM = lastName;
        this.dialogCountryM = dialogCountry;
        this.dialogDistrictM= dialogDistrict;
        this.address1M  = address1;
        this.address2M = address2;
        this.postalCodeM = postalCode;
        this.weightM = weight;
        this.heightM= height;

    }

    public Contact(){

    }

    public Contact( String idNumber0,String posLat, String posLong,String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height,String bcgCheck, String bcgDate, String polioFirstCheck, String polioFirstDate, String polioSecondCheck, String polioSecondDate, String polioThirdCheck, String polioThirdDate, String dptFirstCheck,String dptFirstDate,String dptSecondCheck, String dptSecondDate, String dptThirdCheck, String dptThirdDate, String measleCheck, String measleDate, String jeCheck, String jeDate){

        this.idNumber = idNumber0;
        this.posLat = posLat;
        this.posLong = posLong;
        this.firstNameM = firstName;
        this.middleNameM = middleName;
        this.lastNameM = lastName;
        this.dialogCountryM = dialogCountry;
        this.dialogDistrictM= dialogDistrict;
        this.address1M  = address1;
        this.address2M = address2;
        this.postalCodeM = postalCode;
        this.weightM = weight;
        this.heightM= height;
        this.bcgCheck = bcgCheck;
        this.bcgDate = bcgDate;
        this.polioFirstCheck = polioFirstCheck;
        this.polioFirstDate = polioFirstDate;
        this.polioSecondCheck = polioSecondCheck;
        this.polioSecondDate = polioSecondDate;
        this.polioThirdCheck = polioThirdCheck;
        this.polioThirdDate = polioThirdDate;

        this.dptFirstCheck = dptFirstCheck;
        this.dptFirstDate = dptFirstDate;
        this.dptSecondCheck = dptSecondCheck;
        this.dptSecondDate = dptSecondDate;
        this.dptThirdCheck = dptThirdCheck;
        this.dptThirdDate = dptThirdDate;

        this.measleCheck = measleCheck;
        this.measleDate = measleDate;

        this.jeCheck = jeCheck;
        this.jeDate = jeDate;


    }

    public Contact( String idNumber0,String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height,String bcgCheck, String bcgDate, String polioFirstCheck, String polioFirstDate, String polioSecondCheck, String polioSecondDate, String polioThirdCheck, String polioThirdDate, String dptFirstCheck,String dptFirstDate,String dptSecondCheck, String dptSecondDate, String dptThirdCheck, String dptThirdDate, String measleCheck, String measleDate, String jeCheck, String jeDate){

        this.idNumber = idNumber0;
        this.firstNameM = firstName;
        this.middleNameM = middleName;
        this.lastNameM = lastName;
        this.dialogCountryM = dialogCountry;
        this.dialogDistrictM= dialogDistrict;
        this.address1M  = address1;
        this.address2M = address2;
        this.postalCodeM = postalCode;
        this.weightM = weight;
        this.heightM= height;
        this.bcgCheck = bcgCheck;
        this.bcgDate = bcgDate;
        this.polioFirstCheck = polioFirstCheck;
        this.polioFirstDate = polioFirstDate;
        this.polioSecondCheck = polioSecondCheck;
        this.polioSecondDate = polioSecondDate;
        this.polioThirdCheck = polioThirdCheck;
        this.polioThirdDate = polioThirdDate;

        this.dptFirstCheck = dptFirstCheck;
        this.dptFirstDate = dptFirstDate;
        this.dptSecondCheck = dptSecondCheck;
        this.dptSecondDate = dptSecondDate;
        this.dptThirdCheck = dptThirdCheck;
        this.dptThirdDate = dptThirdDate;

        this.measleCheck = measleCheck;
        this.measleDate = measleDate;

        this.jeCheck = jeCheck;
        this.jeDate = jeDate;


    }



}
