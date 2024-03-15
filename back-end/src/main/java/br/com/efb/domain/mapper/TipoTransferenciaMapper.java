package br.com.efb.domain.mapper;

import br.com.efb.domain.entity.TipoTransferenciaEntity;
import com.efb.api.model.TipoTransferenciaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoTransferenciaMapper {

    TipoTransferenciaMapper INSTANCE = Mappers.getMapper( TipoTransferenciaMapper.class );


//    @Mapping(target = "grupoTransferencia", ignore = true)
    TipoTransferenciaEntity dtoToEntity(TipoTransferenciaDTO tipoTransferenciaDTO);
    @Mapping(source = "grupoTransferencia", target = "grupoTransferencia")
    TipoTransferenciaDTO entityToDTO (TipoTransferenciaEntity tipoTransferenciaEntity);
    @Mapping(target = "grupoTransferencia", ignore = true)
    List<TipoTransferenciaEntity> dtoListToEntityList(List<TipoTransferenciaDTO> tipoTransferenciaDTOS);
    @Mapping(target = "grupoTransferencia", ignore = true)
    List<TipoTransferenciaDTO> entityListToDTOList(List<TipoTransferenciaEntity> tipoTransferenciaEntities);

}
