package docket.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import docket.domain.repository.DocumentoRepository;
import docket.domain.model.Documento;
import docket.domain.exception.DocumentoExistenteException;
import docket.domain.exception.DocumentoNaoEncontradoException;

@Service
public class DocumentoService {
	private DocumentoRepository documentoRepository;

	@Autowired
	public DocumentoService(DocumentoRepository documentoRepository) {
		this.documentoRepository = documentoRepository;
	}

	public List<Documento> listar() {
		return documentoRepository.findAll();
	}

	public List<Documento> listar(String cnpj) {
		List<Documento> documentos = new ArrayList<>();
		for (Documento documento : documentoRepository.findAll()) {
			if (documento.getCartorio().getCnpj().equals(cnpj)) {
				documentos.add(documento);
			}
		}

		return documentos;
	}

	public List<Documento> obterDocumento(String nome) {
		return findOrFail(nome);
	}

	public Documento salvarDocumento(Documento documento) {
		if (findExists(documento))
			throw new DocumentoExistenteException("Documento já existente");

		return documentoRepository.save(documento);
	}

	private List<Documento> findOrFail(String nome) {
		List<Documento> documentosEncontrados = documentoRepository.findByNome(nome);

		if (documentosEncontrados.size() == 0)
			throw new DocumentoNaoEncontradoException("Documento não localizado");

		return documentosEncontrados;
	}

	private Boolean findExists(Documento documento) {
		if (documentoRepository.existsByNome(documento.getNome())) {
			for (Documento documentoEncontrado : documentoRepository.findByNome(documento.getNome())) {
				if (documento.getTipoDocumento().getDescricao()
						.equals(documentoEncontrado.getTipoDocumento().getDescricao())) {
					return true;
				}
			}

			return false;
		}
		return false;
	}
}
