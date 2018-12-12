package com.dof.jaeseonlee.patient.DataProcess;

import android.os.AsyncTask;
import android.util.Log;


import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Observation.*;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt.Constraint;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DeviceComponent;
import ca.uhn.fhir.model.dstu2.resource.DeviceMetric;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Observation.Component;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.DeviceMetricCategoryEnum;
import ca.uhn.fhir.model.dstu2.valueset.DeviceMetricOperationalStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.DeviceStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.rest.client.IGenericClient;


/**
 * Created by 이재선 on 2018-11-26.
 */

public class DataToFhirResources extends AsyncTask<Void,Void,Boolean>{
    String mPatientPhoneNumber;
    Bundle bundle = new Bundle();
    Device device = new Device();
    DeviceComponent deviceComponent1 = new DeviceComponent();
    DeviceComponent deviceComponent2 = new DeviceComponent();
    DeviceComponent deviceComponent3 = new DeviceComponent();
    DeviceMetric deviceMetric = new DeviceMetric();
    Observation observation = new Observation();
    Patient patient = new Patient();

    public DataToFhirResources(String fam,String giv,String phone,boolean isMan,DateDt birth, int hrm){
        makeBundle();
        makePatient(fam, giv, phone, isMan, birth);
        makeDevice();
        makeDeviceComponent();

        makeDeviceMetric();
        makeObservation(hrm);
    }

    public void makeBundle(){
        bundle.setType(BundleTypeEnum.TRANSACTION);
    }

    public void makePatient(String fam, String giv, String phone, boolean isMan, DateDt birth){
        patient.addIdentifier()
                .setSystem("http://www.knu.ac.kr")
                .setValue("KNU0001");
        patient.setActive(true);

        patient.addName()
                .setUse(NameUseEnum.USUAL)
                .addFamily(fam)
                .addGiven(giv);

        patient.addTelecom()
                .setSystem(ContactPointSystemEnum.PHONE)
                .setValue(phone)
                .setUse(ContactPointUseEnum.HOME);

        patient.setGender(isMan?AdministrativeGenderEnum.MALE : AdministrativeGenderEnum.FEMALE);
        patient.setBirthDate(birth);


        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(patient)
                .getRequest()
                .setUrl("Patient")
                .setMethod(HTTPVerbEnum.POST);

    }

    public void makeDevice(){
        device.addIdentifier()
                .setSystem("http://www.jaeseon.co.kr")
                .setValue("device001");

        device.setStatus(DeviceStatusEnum.AVAILABLE);
        device.setId("1");

        CodeableConceptDt codeableConcept = new CodeableConceptDt();
        codeableConcept.addCoding().setSystem("urn:iso:std:iso:11073:10101")
                .setCode("4172")
                .setDisplay("MDC_DEV_ANALY_PRESS_BLD");
        device.setType(codeableConcept);
        device.setLotNumber("20181130001");
        device.setManufacturer("Co.Jaeseon");
        device.setModel("HRM01");
        device.setPatient(new ResourceReferenceDt(patient));

        //.setFullUrl("Device/" + UUID.randomUUID().toString())

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(device)
                .getRequest()
                .setUrl("device")
                .setMethod(HTTPVerbEnum.POST);




    }

    public void makeDeviceComponent(){
        IdentifierDt identifier = new IdentifierDt();
        identifier.setSystem("http://www.jaeseon.co.kr")
                .setValue("mds001");

        deviceComponent1.setIdentifier(identifier);
        deviceComponent1.setId("DeviceComponent/1");

        CodeableConceptDt codeableConcept = new CodeableConceptDt();
        codeableConcept.addCoding().setSystem("urn:iso:std:iso:11073:10101")
                .setCode("1234")
                .setDisplay("MDC_BLD_PLUS_RATE");
        deviceComponent1.setType(codeableConcept);
        ResourceReferenceDt reference = new ResourceReferenceDt(bundle.getEntry().get(1).getFullUrl());
        deviceComponent1.setSource(reference);

        ArrayList<CodeableConceptDt> coding = new ArrayList<CodeableConceptDt>();
        CodeableConceptDt code = new CodeableConceptDt();
        code.addCoding().setCode("on");
        deviceComponent1.setOperationalStatus(coding);

        //		.setFullUrl("urn:uuid:" + UUID.randomUUID().toString())

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(deviceComponent1)
                .getRequest()
                .setUrl("DeviceComponent")
                .setMethod(HTTPVerbEnum.POST);
    }

    public void makeDeviceMetric(){
        IdentifierDt identifier = new IdentifierDt();
        identifier.setSystem("http://www.vanilla.kr")
                .setValue("metric001");
        deviceMetric.setIdentifier(identifier);

        CodeableConceptDt typeCodeableConcept = new CodeableConceptDt();
        typeCodeableConcept.addCoding().setSystem("https://rtmms.nist.gov")
                .setCode("1234")
                .setDisplay("MDC_BLD_PLUS_RATE");
        deviceMetric.setType(typeCodeableConcept);

        CodeableConceptDt unitCodeableConcept = new CodeableConceptDt();
        unitCodeableConcept.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("33232")
                .setDisplay("MDC_HRM_RATE");
        deviceMetric.setUnit(unitCodeableConcept);

        deviceMetric.setSource(new ResourceReferenceDt(bundle.getEntry().get(1).getFullUrl()));
        deviceMetric.setParent(new ResourceReferenceDt(bundle.getEntry().get(2).getFullUrl()));

        deviceMetric.setOperationalStatus(DeviceMetricOperationalStatusEnum.ON);
        deviceMetric.setCategory(DeviceMetricCategoryEnum.MEASUREMENT);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(deviceMetric)
                .getRequest()
                .setUrl("DeviceMetric")
                .setMethod(HTTPVerbEnum.POST);
    }

    public void makeObservation(int hrm){
        IdentifierDt identifier = new IdentifierDt();
        identifier.setSystem("http://www.knu.ac.kr")
                .setValue("hrm001");
        observation.addIdentifier(identifier);
        observation.setStatus(ObservationStatusEnum.REGISTERED);

        CodeableConceptDt CodeableConcept = new CodeableConceptDt();
        CodeableConcept.addCoding().setSystem("https://rtmms.nist.gov")
                .setCode("33232")
                .setDisplay("MDC_HRM_RATE");
        observation.setCode(CodeableConcept);

        observation.setSubject(new ResourceReferenceDt(bundle.getEntry().get(0).getFullUrl()));

        CodeableConceptDt bodyCode = new CodeableConceptDt();
        bodyCode.addCoding()
                .setSystem("http://snomed.info/sct")
                .setCode("368209003");
        observation.setBodySite(bodyCode);

        observation.setDevice(new ResourceReferenceDt(bundle.getEntry().get(1).getFullUrl()));

        QuantityDt sysValue = new QuantityDt();
        sysValue.setValue(hrm).
                setUnit("/M");
        CodeableConceptDt compoCode1 = new CodeableConceptDt();
        compoCode1.addCoding()
                .setSystem("https://rtmms.nist.gov")
                .setCode("33232")
                .setDisplay("MDC_HRM_RATE");
        Component compo1 = observation.addComponent();
        compo1.setCode(compoCode1)
                .setValue(sysValue);

        bundle.addEntry()
                .setFullUrl("urn:uuid:" + UUID.randomUUID().toString())
                .setResource(observation)
                .getRequest()
                .setUrl("Observation")
                .setMethod(HTTPVerbEnum.POST);
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
    public DeviceComponent getComponent2() {
        return deviceComponent2;
    }
    public void setComponent2(DeviceComponent component2) {
        this.deviceComponent2 = component2;
    }
    public DeviceComponent getComponent3() {
        return deviceComponent3;
    }
    public void setComponent3(DeviceComponent component3) {
        this.deviceComponent3 = component3;
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

        FhirContext ctx = FhirContext.forDstu2();
        // Create a client and post the transaction to the server
        IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
        try {
            Bundle resp = client.transaction().withBundle(bundle).execute();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        POSTMessage();
        return null;
    }



}
