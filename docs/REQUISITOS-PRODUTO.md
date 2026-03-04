# Requisitos de produto — SetPoint

Documento de referência para requisitos que atravessam várias specs e influenciam arquitetura e implementação.

---

## Offline-first / cache

O app deve **funcionar o melhor possível offline**. Ou seja:

- **Cache local obrigatório**: dados necessários para uso diário (treinos atribuídos, exercícios, histórico de execução, etc.) devem ser armazenados localmente no dispositivo.
- **Uso sem rede**: o usuário (aluno e professor) deve conseguir visualizar treinos, registrar execuções e fazer ações essenciais mesmo sem conexão; as operações devem ser persistidas localmente e sincronizadas quando houver rede.
- **Sincronização**: quando online, o app deve sincronizar com o backend (enviar dados pendentes, receber atualizações); a estratégia concreta (ex.: última escrita vence, merge por entidade) será definida nas specs de data/sync.
- **Indicador de estado**: a UI pode informar ao usuário quando está offline e quando há dados pendentes de sincronização (detalhes em specs de apresentação).

### Impacto nas specs

- **Domínio (specs 01–03)**: não mudam; continuam puros e sem referência a rede ou persistência.
- **Camada de dados (futuras specs)**: repositórios devem considerar **fonte local como primária** (leitura/escrita no cache) e backend como sincronização; definir armazenamento local (ex.: SQLDelight, Realm, DataStore) e política de sync.
- **API / rede**: camada de rede a serviço da sincronização e do carregamento inicial; não substituir a leitura do cache para fluxos que precisam funcionar offline.

Ao redigir ou implementar specs de **data**, **sync** ou **API**, usar este documento como restrição: o desenho deve permitir uso offline e cache local como fonte primária de leitura.

---

*Última atualização: 2025-03-03*
