package flutecode.RentalCarBookingSystem.Mapper;

import flutecode.RentalCarBookingSystem.dto.UserRequestDto;
import flutecode.RentalCarBookingSystem.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    UserEntity toUserEntity(UserRequestDto userReqDto);
}