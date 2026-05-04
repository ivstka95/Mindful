#!/usr/bin/env bash
set -euo pipefail

branch="$(git rev-parse --abbrev-ref HEAD)"

case "$branch" in
  main|master)
    echo "Direct commits to '$branch' are not allowed." >&2
    echo "Create a feature branch: git checkout -b <feature-name>" >&2
    echo "(Bypass with --no-verify only if you know what you're doing.)" >&2
    exit 1
    ;;
esac
