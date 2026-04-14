# SCF - Sistema de Controle Financeiro

Sistema pessoal de controle financeiro. Backend em Spring Boot 3, frontend em Vue 3.

## Estrutura do Projeto

```
nscf/
├── backend/    Spring Boot 3.4.x REST API (Java 21, Maven)
└── frontend/   Vue 3 SPA (Vite, TypeScript, PrimeVue 4)
```

## Stack Tecnológica

| Camada    | Tecnologia                                       |
|-----------|--------------------------------------------------|
| Frontend  | Vue 3.5+, Vite 6, TypeScript, Pinia, Vue Router  |
| UI        | PrimeVue 4 (tema Aura)                           |
| HTTP      | Axios                                            |
| Backend   | Spring Boot 3.4+, Java 21                        |
| ORM       | Spring Data JPA + Hibernate                      |
| Banco     | SQL Server 2022                                  |
| Segurança | Spring Security + JWT (JJWT 0.12.x)             |
| Build     | Maven (backend), Vite (frontend)                 |

## Comandos de Desenvolvimento

### Backend

```bash
# Rodar backend (a partir da raiz do projeto)
cd backend && ./mvnw spring-boot:run

# Rodar testes
cd backend && ./mvnw test

# Gerar JAR
cd backend && ./mvnw clean package -DskipTests
```

### Frontend

```bash
# Instalar dependências (primeira vez)
cd frontend && npm install

# Rodar servidor de desenvolvimento (porta 5173)
cd frontend && npm run dev

# Build para produção
cd frontend && npm run build

# Type check
cd frontend && npm run type-check
```

### Rodando os Dois Juntos

Abra dois terminais:
- Terminal 1: `cd backend && ./mvnw spring-boot:run`
- Terminal 2: `cd frontend && npm run dev`

Backend: http://localhost:8080
Frontend: http://localhost:5173 (proxy `/api` → backend)

## Convenções do Backend

### Estrutura de Pacotes

```
br.scf.
├── config/         Configurações (Security, CORS, etc.)
├── controller/     REST controllers (camada HTTP)
├── service/        Regras de negócio
├── repository/     JPA repositories (acesso ao banco)
├── entity/         Entidades JPA (@Entity)
└── dto/            Request/Response DTOs (nunca expor entity diretamente)
```

### Padrões REST

```
GET    /api/v1/{recurso}        listar todos
GET    /api/v1/{recurso}/{id}   buscar por id
POST   /api/v1/{recurso}        criar
PUT    /api/v1/{recurso}/{id}   atualizar completo
PATCH  /api/v1/{recurso}/{id}   atualizar parcial
DELETE /api/v1/{recurso}/{id}   deletar
```

### Regras

- Controllers são finos: apenas recebem request e delegam ao Service
- Services contêm toda a lógica de negócio
- DTOs sempre para request e response — nunca expor Entity diretamente
- IDs são `Long` com `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Validação via anotações Jakarta (`@NotNull`, `@NotBlank`, `@Positive`, etc.)
- Exception handling global via `@RestControllerAdvice`

## Convenções do Frontend

- **Linguagem:** TypeScript em todos os arquivos (nenhum `.js`)
- **Componentes:** `<script setup lang="ts">` (Composition API, sem Options API)
- **Estado global:** Pinia stores em `src/stores/`
- **Estado local:** `ref` e `reactive` diretamente no componente
- **Chamadas HTTP:** sempre via `src/services/api.ts` — nunca Axios direto em componentes
- **Tipos:** definir interfaces em `src/types/index.ts`
- **Nomenclatura:**
  - Componentes: PascalCase (`TransacaoForm.vue`)
  - Composables: prefixo `use` (`useTransacao.ts`)
  - Stores: `useXxxStore` (`useAuthStore`)
  - Views: sufixo `View` (`DashboardView.vue`)

## Migrations de Banco (Flyway)

Arquivos em `backend/src/main/resources/db/migration/`.

Nomenclatura obrigatória: `V{numero}__{descricao}.sql`

```
V1__baseline.sql          ponto de partida
V2__cria_tabela_usuario.sql
V3__cria_tabela_conta.sql
V4__add_coluna_saldo.sql
```

- Nunca edite um arquivo de migration já aplicado ao banco
- Sempre crie um novo arquivo para qualquer alteração de schema
- O Flyway aplica automaticamente ao subir a aplicação
- Tabela `flyway_schema_history` no banco rastreia o histórico

## Workflow para Nova Funcionalidade

1. Criar entity em `backend/.../entity/`
2. Criar repository em `backend/.../repository/`
3. Criar service + testes em `backend/.../service/`
4. Criar controller em `backend/.../controller/`
5. Definir tipos TypeScript em `frontend/src/types/index.ts`
6. Criar métodos de API em `frontend/src/services/api.ts`
7. Criar Pinia store se estado precisar ser compartilhado
8. Criar View e Components
9. Registrar rota em `frontend/src/router/index.ts`
10. Marcar funcionalidade como concluída no `FEATURES.md`

## Convenções Git

- Branches: `feature/nome-da-feature`, `fix/descricao-do-bug`
- Commits em português (projeto pessoal BR): `feat: adiciona tela de lançamentos`
- Estilo Conventional Commits: `feat:`, `fix:`, `refactor:`, `docs:`, `chore:`
