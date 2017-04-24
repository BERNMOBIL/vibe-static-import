package ch.bernmobil.vibe.staticdata.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsAgency;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AgencyFieldSetMapper implements FieldSetMapper<GtfsAgency> {

    @Override
    public GtfsAgency mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsAgency agency = new GtfsAgency();
        agency.setAgencyId(fieldSet.readString("agency_id"));
        agency.setAgencyName(fieldSet.readString("agency_name"));
        agency.setAgencyUrl(fieldSet.readString("agency_url"));
        agency.setAgencyTimezone(fieldSet.readString("agency_timezone"));
        agency.setAgencyLang(fieldSet.readString("agency_lang"));
        agency.setAgencyPhone(fieldSet.readString("agency_phone"));
        return agency;
    }
}
