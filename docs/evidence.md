# 团队证据索引表

## 仓库
- 仓库地址：https://github.com/zhaojeak/campus-todo-team-3
- 成员：
  - zhaojeak（仓库管理员）
  - Sherpherd（质量负责人）
  - Arthur20060209（开发者 A）
  - Can0401（开发者 B）

## Milestone
- Sprint 1 - Team Collaboration

## Issues
- #1 任务优先级筛选：https://github.com/zhaojeak/campus-todo-team-3/issues/1
- #2 任务完成与重复完成校验：https://github.com/zhaojeak/campus-todo-team-3/issues/2
- #3 增加 Maven CI 与协作指南：https://github.com/zhaojeak/campus-todo-team-3/issues/3

## Pull Requests
- PR #6（CI 配置）：https://github.com/zhaojeak/campus-todo-team-3/pull/6
- PR #7（优先级筛选）：https://github.com/zhaojeak/campus-todo-team-3/pull/7
- PR #8（任务完成 + 冲突）：https://github.com/zhaojeak/campus-todo-team-3/pull/8

## Releases
- v0.1.0（基线）：https://github.com/zhaojeak/campus-todo-team-3/releases/tag/v0.1.0
- v1.0.0（最终）：https://github.com/zhaojeak/campus-todo-team-3/releases/tag/v1.0.0

## CI 记录
- Actions 页面：https://github.com/zhaojeak/campus-todo-team-3/actions

## 冲突解决记录
- PR #8 包含冲突解决提交：`fix: resolve README capability conflict`
- 冲突文件：
  - README.md
  - src/main/java/edu/hbuas/campustodo/service/TaskService.java
  - src/test/java/edu/hbuas/campustodo/service/TaskServiceTest.java
- 最终 README 固定句：`当前版本：支持新增、列出、按优先级筛选和完成任务。`

## 关键提交
- `chore: initialize CampusTodo baseline`（v0.1.0 基线）
- `test: specify priority filtering behavior`（PR #7 测试）
- `feat: filter tasks by priority`（PR #7 功能）
- `test: specify task completion rules`（PR #8 失败测试）
- `feat: complete a task by id`（PR #8 功能）
- `ci: verify Java project on pull requests`（PR #6 CI 配置）
- `fix: resolve README capability conflict`（PR #8 冲突解决）
- `fix: add missing imports after merge conflict resolution`（PR #8 CI 修复）
