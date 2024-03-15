package br.com.efb.domain.mapper;

import br.com.efb.domain.entity.ContaEntity;
import com.efb.api.model.ContaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContaMapper {

    ContaMapper INSTANCE = Mappers.getMapper( ContaMapper.class );

    ContaEntity dtoToEntity(ContaDTO contaDTO);

    ContaDTO entityToDTO (ContaEntity contaEntity);

    List<ContaEntity> dtoListToEntityList(List<ContaDTO> contaDTOS);

    List<ContaDTO> entityListToDTOList(List<ContaEntity> contaEntities);

}
