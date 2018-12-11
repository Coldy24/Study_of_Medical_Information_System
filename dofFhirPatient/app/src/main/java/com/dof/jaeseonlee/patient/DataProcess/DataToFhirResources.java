package com.dof.jaeseonlee.patient.DataProcess;

import android.os.AsyncTask;
import android.util.Log;


import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Observation.*;
import org.hl7.fhir.dstu3.model.Device.*;



import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * Created by 이재선 on 2018-11-26.
 */

public class DataToFhirResources extends AsyncTask<Void,Void,Boolean>{
    String mPatientPhoneNumber;
    Bundle bundle = new Bundle();
    Device device = new Device();
    DeviceComponent deviceComponent1 = new DeviceComponent();
    DeviceMetric deviceMetric = new DeviceMetric();
    Observation observation = new Observation();
    Patient patient = new Patient();


    /* 생성자 */
    public DataToFhirResources(String fam, String giv, String phone, boolean isMan, Date birth, int hrm){
        this.mPatientPhoneNumber = phone;
        makeBundle();
        makePatient(fam, giv, phone, isMan, birth);
        makeDevice();
        makeDeviceComponent();
        makeDeviceMetric();
        makeObservation(hrm);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        POSTMessage();

        return false;
    }


    public void makeBundle(){
        bundle.setType(Bundle.BundleType.TRANSACTION);
    }

    public void makePatient(String fam, String giv, String phone, boolean isMan, Date birth){
        patient.addIdentifier()
                .setSystem("http://www.knu.ac.kr")
                .setValue("KNU0001");
        patient.setActive(true);

        patient.addName()
                .setUse(HumanName.NameUse.USUAL)
                .setFamily(fam)
                .addGiven(giv);

        patient.addTelecom()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(phone)
                .setUse(ContactPoint.ContactPointUse.HOME);

        patient.setGender(isMan?Enumerations.AdministrativeGender.MALE : Enumerations.AdministrativeGender.FEMALE);
        patient.setBirthDate(birth);


        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(patient)
                .getRequest()
                .setUrl("Patient");

       // FhirContext ctx = FhirContext.forDstu3();
        //String patientString =ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
        //Log.e("하잉페이션트",patientString);

    }

    public void makeDevice(){
        device.addIdentifier()
                .setSystem("http://www.jaeseon.co.kr")
                .setValue("device001");

        device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
        device.setId("1");

        CodeableConcept codeableConcept = new CodeableConcept();
        codeableConcept.addCoding().setSystem("urn:iso:std:iso:11073:10101")
                .setCode("4172")
                .setDisplay("MDC_DEV_ANALY_PRESS_BLD");
        device.setType(codeableConcept);
        device.setLotNumber("20181130001");
        device.setManufacturer("Co.Jaeseon");
        device.setManufactureDate(new Date(2018,11,1));
        device.setModel("HRM01");
        device.setPatient(new Reference(patient));

        //.setFullUrl("Device/" + UUID.randomUUID().toString())

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(device)
                .getRequest()
                .setUrl("device");


    }

    public void makeDeviceComponent(){
        Identifier identifier = new Identifier();
        identifier.setSystem("http://www.jaeseon.co.kr")
                .setValue("mds001");

        deviceComponent1.setIdentifier(identifier);
        deviceComponent1.setId("DeviceComponent/1");

        CodeableConcept codeableConcept = new CodeableConcept();
        codeableConcept.addCoding().setSystem("urn:iso:std:iso:11073:10101")
                .setCode("1234")
                .setDisplay("MDC_BLD_PLUS_RATE");
        deviceComponent1.setType(codeableConcept);
        deviceComponent1.setLastSystemChange(new Date(2018,12,3));
        Reference reference = new Reference(bundle.getEntry().get(1).getFullUrl());
        deviceComponent1.setSource(reference);

        ArrayList<CodeableConcept> coding = new ArrayList<CodeableConcept>();
        CodeableConcept code = new CodeableConcept();
        code.addCoding().setCode("on");
        deviceComponent1.setOperationalStatus(coding);

        //		.setFullUrl("urn:uuid:" + UUID.randomUUID().toString())

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(deviceComponent1)
                .getRequest()
                .setUrl("DeviceComponent");
    }

    public void makeDeviceMetric(){
        Identifier identifier = new Identifier();
        identifier.setSystem("http://www.vanilla.kr")
                .setValue("metric001");
        deviceMetric.setIdentifier(identifier);

        CodeableConcept typeCodeableConcept = new CodeableConcept();
        typeCodeableConcept.addCoding().setSystem("https://rtmms.nist.gov")
                .setCode("1234")
                .setDisplay("MDC_BLD_PLUS_RATE");
        deviceMetric.setType(typeCodeableConcept);

        CodeableConcept unitCodeableConcept = new CodeableConcept();
        unitCodeableConcept.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("33232")
                .setDisplay("MDC_HRM_RATE");
        deviceMetric.setUnit(unitCodeableConcept);

        deviceMetric.setSource(new Reference(device));
        deviceMetric.setParent(new Reference(patient));

        deviceMetric.setOperationalStatus(DeviceMetric.DeviceMetricOperationalStatus.ON);
        deviceMetric.setCategory(DeviceMetric.DeviceMetricCategory.MEASUREMENT);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(deviceMetric)
                .getRequest()
                .setUrl("DeviceMetric");
    }

    public void makeObservation(int hrm){
        Identifier identifier = new Identifier();
        identifier.setSystem("http://www.knu.ac.kr")
                .setValue("hrm001");
        observation.addIdentifier(identifier);
        observation.setStatus(ObservationStatus.REGISTERED);

        CodeableConcept CodeableConcept = new CodeableConcept();
        CodeableConcept.addCoding().setSystem("https://rtmms.nist.gov")
                .setCode("33232")
                .setDisplay("MDC_HRM_RATE");
        observation.setCode(CodeableConcept);

        //0은 Patient
        observation.setSubject(new Reference(patient));

        CodeableConcept bodyCode = new CodeableConcept();
        bodyCode.addCoding()
                .setSystem("http://snomed.info/sct")
                .setCode("368209003");
        observation.setBodySite(bodyCode);

        observation.setDevice(new Reference(device));

        Quantity sysValue = new Quantity();
        sysValue.setValue(hrm).
                setUnit("/M");
        CodeableConcept compoCode1 = new CodeableConcept();
        compoCode1.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("33232")
                .setDisplay("MDC_HRM_RATE");
        Observation.ObservationComponentComponent compo1 = observation.addComponent();
        compo1.setCode(compoCode1)
                .setValue(sysValue);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(observation)
                .getRequest()
                .setUrl("Observation");
    }

    //getter and setter
    public Bundle getBundle() {
        return bundle;
    }
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
    }
    public DeviceComponent getComponent1() {
        return deviceComponent1;
    }
    public void setComponent1(DeviceComponent component1) {
        this.deviceComponent1 = component1;
    }
    public DeviceMetric getDeviceMetric() {
        return deviceMetric;
    }
    public void setDeviceMetric(DeviceMetric deviceMetric) {
        this.deviceMetric = deviceMetric;
    }

    public Observation getObservation() {
        return observation;
    }
    public void setObservation(Observation observation) {
        this.observation = observation;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }



    public void POSTMessage(){
        FhirContext context = FhirContext.forDstu3();
        IGenericClient client = context.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
        client.transaction().withBundle(bundle).execute();

    }


}
