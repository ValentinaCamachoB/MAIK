package com.carvajal.wishlist.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
import org.springframework.stereotype.Service;
 
import com.carvajal.wishlist.dto.ActualizarDeseoRequestDTO;
import com.carvajal.wishlist.dto.AgregarDeseoRequestDTO;
import com.carvajal.wishlist.dto.HistoricoResponseDTO;
import com.carvajal.wishlist.dto.HttpGlobalResponse;
import com.carvajal.wishlist.dto.ItemDeseoResponseDTO;
import com.carvajal.wishlist.dto.MessageResponseDTO;
import com.carvajal.wishlist.entity.HistoricoListaDeseos;
import com.carvajal.wishlist.entity.ListaDeseos;
import com.carvajal.wishlist.entity.Producto;
import com.carvajal.wishlist.repository.HistoricoListaDeseosRepository;
import com.carvajal.wishlist.repository.ListaDeseosRepository;
import com.carvajal.wishlist.repository.ProductoRepository;
import com.carvajal.wishlist.repository.UsuarioRepository;
 
import lombok.RequiredArgsConstructor;
 
@Service
@RequiredArgsConstructor
public class ListaDeseosService {
 
    private final ListaDeseosRepository listaDeseosRepository;
    private final ProductoRepository productoRepository;
    private final HistoricoListaDeseosRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
 

    private static final String ACCION_AGREGADO = "AGREGADO";
    private static final String ACCION_ELIMINADO = "ELIMINADO";
    private static final String ACCION_ACTUALIZADO = "ACTUALIZADO";
 
    public HttpGlobalResponse<List<ItemDeseoResponseDTO>> listarDeseos(Long idUsuario) {
        HttpGlobalResponse<List<ItemDeseoResponseDTO>> response = new HttpGlobalResponse<>();
 
        List<ListaDeseos> items = listaDeseosRepository.findByIdUsuario(idUsuario);
 
        if (items.isEmpty()) {
            response.setMessage("La lista de deseos esta vacia");
            response.setData(new ArrayList<>());
            return response;
        }
 
        List<ItemDeseoResponseDTO> resultado = new ArrayList<>();
 
        for (ListaDeseos item : items) {
            Optional<Producto> productoOpt = productoRepository.findById(item.getIdProducto());
 
            if (productoOpt.isEmpty()) {
                continue;
            }
 
            Producto producto = productoOpt.get();
 
            ItemDeseoResponseDTO dto = new ItemDeseoResponseDTO();
            dto.setIdListaDeseos(item.getIdListaDeseos());
            dto.setIdProducto(producto.getIdProducto());
            dto.setNombreProducto(producto.getNombre());
            dto.setDescripcion(producto.getDescripcion());
            dto.setPrecio(producto.getPrecio());
            dto.setStockActual(producto.getStock());
            dto.setCantidadDeseada(item.getCantidad());
            dto.setFechaAgregado(item.getFechaAgregado());
 
            if (producto.getStock() == null || producto.getStock() <= 0) {
                dto.setSinStock(true);
                dto.setMensajeNotificacion("Este producto ya no se encuentra en stock");
            } else if (producto.getStock() < item.getCantidad()) {
                dto.setSinStock(true);
                dto.setMensajeNotificacion("Solo quedan " + producto.getStock()
                        + " unidades disponibles (deseas " + item.getCantidad() + ")");
            } else {
                dto.setSinStock(false);
            }
 
            resultado.add(dto);
        }
 
        response.setMessage("Lista de deseos obtenida correctamente");
        response.setData(resultado);
        return response;
    }
 
    public MessageResponseDTO agregarDeseo(AgregarDeseoRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();
 
        if (usuarioRepository.findById(request.getIdUsuario()).isEmpty()) {
            response.setMessage("El usuario no existe");
            return response;
        }
 
        Optional<Producto> productoOpt = productoRepository.findById(request.getIdProducto());
        if (productoOpt.isEmpty()) {
            response.setMessage("El producto no existe");
            return response;
        }
 
        Optional<ListaDeseos> existente = listaDeseosRepository
                .findByIdUsuarioAndIdProducto(request.getIdUsuario(), request.getIdProducto());
 
        if (existente.isPresent()) {
            response.setMessage("Este producto ya esta en tu lista de deseos");
            return response;
        }
 
        ListaDeseos item = new ListaDeseos();
        item.setIdUsuario(request.getIdUsuario());
        item.setIdProducto(request.getIdProducto());
        item.setCantidad(request.getCantidad());
        item.setFechaAgregado(LocalDateTime.now());
        listaDeseosRepository.save(item);
 
        guardarHistorico(request.getIdUsuario(), request.getIdProducto(),
                request.getCantidad(), ACCION_AGREGADO);
 
        response.setMessage("Producto agregado a la lista de deseos correctamente");
        return response;
    }
 
    public MessageResponseDTO actualizarDeseo(Long idListaDeseos, ActualizarDeseoRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();
 
        Optional<ListaDeseos> itemOpt = listaDeseosRepository.findById(idListaDeseos);
        if (itemOpt.isEmpty()) {
            response.setMessage("Item no encontrado");
            return response;
        }
 
        ListaDeseos item = itemOpt.get();
        item.setCantidad(request.getCantidad());
        listaDeseosRepository.save(item);
 
        guardarHistorico(item.getIdUsuario(), item.getIdProducto(),
                request.getCantidad(), ACCION_ACTUALIZADO);
 
        response.setMessage("Item actualizado correctamente");
        return response;
    }
 
    public MessageResponseDTO eliminarDeseo(Long idListaDeseos) {
        MessageResponseDTO response = new MessageResponseDTO();
 
        Optional<ListaDeseos> itemOpt = listaDeseosRepository.findById(idListaDeseos);
        if (itemOpt.isEmpty()) {
            response.setMessage("Item no encontrado");
            return response;
        }
 
        ListaDeseos item = itemOpt.get();
 
        guardarHistorico(item.getIdUsuario(), item.getIdProducto(),
                item.getCantidad(), ACCION_ELIMINADO);
 
        listaDeseosRepository.deleteById(idListaDeseos);
 
        response.setMessage("Item eliminado de la lista de deseos correctamente");
        return response;
    }
 
    public HttpGlobalResponse<List<HistoricoResponseDTO>> getHistorico(Long idUsuario) {
        HttpGlobalResponse<List<HistoricoResponseDTO>> response = new HttpGlobalResponse<>();
 
        List<HistoricoListaDeseos> registros = historicoRepository
                .findByIdUsuarioOrderByFechaAccionDesc(idUsuario);
 
        List<HistoricoResponseDTO> resultado = new ArrayList<>();
 
        for (HistoricoListaDeseos registro : registros) {
            HistoricoResponseDTO dto = new HistoricoResponseDTO();
            dto.setIdHistorico(registro.getIdHistorico());
            dto.setIdProducto(registro.getIdProducto());
            dto.setCantidad(registro.getCantidad());
            dto.setAccion(registro.getAccion());
            dto.setFechaAccion(registro.getFechaAccion());
 
            Optional<Producto> producto = productoRepository.findById(registro.getIdProducto());
            if (producto.isPresent()) {
                dto.setNombreProducto(producto.get().getNombre());
            } else {
                dto.setNombreProducto("(Producto eliminado del catalogo)");
            }
 
            resultado.add(dto);
        }
 
        response.setMessage("Historico obtenido correctamente");
        response.setData(resultado);
        return response;
    }
 
    private void guardarHistorico(Long idUsuario, Long idProducto, Integer cantidad, String accion) {
        HistoricoListaDeseos historico = new HistoricoListaDeseos();
        historico.setIdUsuario(idUsuario);
        historico.setIdProducto(idProducto);
        historico.setCantidad(cantidad);
        historico.setAccion(accion);
        historico.setFechaAccion(LocalDateTime.now());
        historicoRepository.save(historico);
    }
}