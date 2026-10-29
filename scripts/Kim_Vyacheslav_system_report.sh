#!/bin/bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
mkdir -p "$ROOT/logs"
{
 echo 'Ким Вячеслав Александрович | Kim Vyacheslav'
 date -Iseconds 2>/dev/null || date
 whoami
 uname -a
 if [ -r /etc/os-release ]; then cat /etc/os-release; fi
 echo 'CPU and memory'
 if command -v lscpu >/dev/null; then lscpu; else sysctl -n machdep.cpu.brand_string; fi
 if command -v free >/dev/null; then free -h; else vm_stat; fi
 echo 'Disk usage and available space'
 df -h
} | tee "$ROOT/logs/Kim_Vyacheslav_system_report.log"
