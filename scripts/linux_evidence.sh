#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p logs backups evidence
chmod 744 scripts/Kim_Vyacheslav_system_report.sh
touch logs/Kim_Vyacheslav_system_report.log
./scripts/Kim_Vyacheslav_system_report.sh
cp README.md backups/Kim_Vyacheslav_README.md
cmp README.md backups/Kim_Vyacheslav_README.md
ls -l scripts/Kim_Vyacheslav_system_report.sh backups/Kim_Vyacheslav_README.md
find . -maxdepth 3 -not -path './.git/*' -not -path './build/*' -not -path './.gradle/*' | sort
ps -ef
ps -ef | grep '[j]ava' || true
tail -5 logs/Kim_Vyacheslav_system_report.log
grep -E 'Ким|Kim' logs/Kim_Vyacheslav_system_report.log > logs/Kim_Vyacheslav_log_analysis.txt
cat logs/Kim_Vyacheslav_log_analysis.txt
