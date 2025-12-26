package com.friendspark.backend.mapper

import com.friendspark.backend.dto.user.UserCreateDto
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.util.DateTimeUtil
import org.mapstruct.BeanMapping
import org.mapstruct.InjectionStrategy.CONSTRUCTOR
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import org.mapstruct.MappingTarget
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR
)
abstract class UserMapper {

    @Autowired
    lateinit var dateTimeUtil: DateTimeUtil

    @Named("toUser")
    @Mapping(target = "role", expression = "java(com.friendspark.backend.entity.UserRole.USER)")
    @Mapping(target = "lastActiveAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    abstract fun to(dto: UserCreateDto): User

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "lastActiveAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "photoUrl", source = "photoUrl")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "bio", source = "bio")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "interests", source = "interests")
    abstract fun update(@MappingTarget user: User, dto: UserUpdateDTO): User

}