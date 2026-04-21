package in.gov.telangana.agri.farmeridentityservice.mapper;

import in.gov.telangana.agri.farmeridentityservice.dto.request.EnrollmentRequest;
import in.gov.telangana.agri.farmeridentityservice.dto.response.FarmerResponse;
import in.gov.telangana.agri.farmeridentityservice.entity.Farmer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FarmerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "landProfiles", ignore = true)
    @Mapping(target = "aadharVerifications", ignore = true)
    Farmer toEntity(EnrollmentRequest request);

    @Mapping(target = "aadhaarLastFourDigits", expression = "java(farmer.getAadhaarNumber().substring(8))")
    FarmerResponse toResponse(Farmer farmer);
}
    
    