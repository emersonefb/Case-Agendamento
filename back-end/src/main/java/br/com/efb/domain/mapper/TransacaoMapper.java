package br.com.efb.domain.mapper;

import br.com.efb.domain.entity.TransacaoEntity;
import com.efb.api.model.TransacaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    TransacaoMapper INSTANCE = Mappers.getMapper( TransacaoMapper.class );

    TransacaoEntity dtoToEntity(TransacaoDTO TransacaoDTO);

    TransacaoDTO entityToDTO (TransacaoEntity TransacaoEntity);

    List<TransacaoEntity> dtoListToEntityList(List<TransacaoDTO> TransacaoDTOS);

    List<TransacaoDTO> entityListToDTOList(List<TransacaoEntity> TransacaoEntities);

}
