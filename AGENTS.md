# AGENTS.md

本项目将 AI 作为"受约束的工程助手"，而不是自由代码生成器。

## 协作顺序

1. 先明确目标、范围、假设。
2. 先给简短计划，再开始多文件修改。
3. 每次只做一个小步骤，便于审查。
4. 每步结束后说明改了什么、怎么验证、还有什么风险。

## 工程约束

- 保持分层：controller -> application -> domain -> infrastructure
- 不要把核心业务逻辑写进 DTO、util 或 controller。

## 回退规则

- 需求变化时先更新spec.md，核心模型变化时先更新design.md，范围变化时先更新tasks.md。
