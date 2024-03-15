package br.com.efb.domain.mapper;

import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import com.efb.api.model.GrupoTransferenciaCadastroDTO;
import com.efb.api.model.GrupoTransferenciaDTO;
import com.efb.api.model.GrupoTransferenciaResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GrupoTransferenciaMapper {

    GrupoTransferenciaMapper INSTANCE = Mappers.getMapper( GrupoTransferenciaMapper.class );

    GrupoTransferenciaEntity dtoToEntity(GrupoTransferenciaDTO grupoTransferenciaDTO);
    GrupoTransferenciaEntity dtoResultToEntity(GrupoTransferenciaResultDTO grupoTransferenciaResultDTO);

    GrupoTransferenciaDTO entityToDTO (GrupoTransferenciaEntity grupoTransferenciaEntity);
    GrupoTransferenciaResultDTO entityToResultDTO (GrupoTransferenciaEntity grupoTransferenciaEntity);

    List<GrupoTransferenciaEntity> dtoListToEntityList(List<GrupoTransferenciaDTO> grupoTransferenciaDTOS);


//    @Mapping(target = "tipoTransferenciaList", ignore = true)
    List<GrupoTransferenciaDTO> entityListToDTOList(List<GrupoTransferenciaEntity> grupoTransferenciaEntities);

    GrupoTransferenciaEntity dtoToEntityCadastro(GrupoTransferenciaCadastroDTO body);
}
