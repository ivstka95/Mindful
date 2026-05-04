#!/bin/bash
# Runs before any Bash command. If the command is git-related, scan for secrets.
TOOL_INPUT=$(cat)
COMMAND=$(echo "$TOOL_INPUT" | jq -r '.tool_input.command // ""')

if [[ "$COMMAND" == *"git add"* || "$COMMAND" == *"git commit"* ]]; then
    if command -v gitleaks &> /dev/null; then
        if ! gitleaks protect --staged --no-banner --redact 2>&1; then
            echo "ERROR: gitleaks detected secrets in staged files. Aborting." >&2
            exit 2
        fi
    fi
fi
exit 0
