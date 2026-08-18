"""
run_all_benchmarks.py
Group 15: Codebility v2.0
ECG Dumsor Response Optimizer — Performance Benchmarking & Visualization Suite
"""

import os
import subprocess
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Find Java 21
def get_java_cmd():
    for base in [r'C:\Program Files\Java', r'C:\Program Files\Eclipse Adoptium', r'C:\Users\HP\.jdk']:
        if os.path.exists(base):
            for root, dirs, files in os.walk(base):
                if 'java.exe' in files and ('21' in root or 'jdk-21' in root or 'temurin-21' in root):
                    return os.path.join(root, 'java.exe')
    return 'java'

def run_java_experiments():
    java_cmd = get_java_cmd()
    print(f"Running Experiments.java using {java_cmd}...")
    res = subprocess.run([java_cmd, '-cp', 'target/classes', 'com.g15.dsa.experiments.Experiments'], capture_output=True, text=True)
    print(res.stdout)
    if res.stderr:
        print("STDERR:", res.stderr)

def generate_charts():
    os.makedirs('reports/graphs', exist_ok=True)
    plt.style.use('seaborn-v0_8-whitegrid' if 'seaborn-v0_8-whitegrid' in plt.style.available else 'default')

    # 1. Search & Sort Runtime Graph
    if os.path.exists('data/sort_experiment.csv') and os.path.exists('data/search_experiment.csv'):
        df_sort = pd.read_csv('data/sort_experiment.csv')
        df_search = pd.read_csv('data/search_experiment.csv')

        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5))
        
        # Sort curves
        for algo in df_sort['algorithm'].unique():
            sub = df_sort[df_sort['algorithm'] == algo].groupby('input_size')['time_ms'].mean().reset_index()
            ax1.plot(sub['input_size'], sub['time_ms'], marker='o', label=algo, linewidth=2)
        ax1.set_title('Sorting Algorithms Runtime Comparison', fontsize=12, fontweight='bold')
        ax1.set_xlabel('Input Size (n)')
        ax1.set_ylabel('Mean Execution Time (ms)')
        ax1.legend()
        ax1.grid(True, linestyle='--', alpha=0.6)

        # Search curves
        for algo in df_search['algorithm'].unique():
            sub = df_search[df_search['algorithm'] == algo].groupby('input_size')['time_ms'].mean().reset_index()
            ax2.plot(sub['input_size'], sub['time_ms'], marker='s', label=algo, linewidth=2)
        ax2.set_title('Search Algorithms Runtime (Linear vs Binary)', fontsize=12, fontweight='bold')
        ax2.set_xlabel('Input Size (n)')
        ax2.set_ylabel('Mean Execution Time (ms)')
        ax2.legend()
        ax2.grid(True, linestyle='--', alpha=0.6)

        plt.tight_layout()
        plt.savefig('reports/graphs/ECG_runtime_performance_graph.png', dpi=300)
        plt.close()
        print("Generated: reports/graphs/ECG_runtime_performance_graph.png")

    # 2. Hash Table Collision & Load Factor Graph
    if os.path.exists('data/hash_experiment.csv'):
        df_hash = pd.read_csv('data/hash_experiment.csv')
        fig, ax = plt.subplots(figsize=(8, 5))
        ax.plot(df_hash['keys_inserted'], df_hash['collisions'], marker='^', color='#d62728', linewidth=2, label='Collisions')
        ax.set_title('Custom HashTable Collision Count vs Key Count', fontsize=12, fontweight='bold')
        ax.set_xlabel('Keys Inserted (n)')
        ax.set_ylabel('Collision Count')
        ax.legend()
        ax.grid(True, linestyle='--', alpha=0.6)
        plt.tight_layout()
        plt.savefig('reports/graphs/ECG_hash_collision_graph.png', dpi=300)
        plt.close()
        print("Generated: reports/graphs/ECG_hash_collision_graph.png")

    # 3. Heap Priority Dispatch Graph
    if os.path.exists('data/heap_experiment.csv'):
        df_heap = pd.read_csv('data/heap_experiment.csv')
        fig, ax = plt.subplots(figsize=(8, 5))
        for op in df_heap['operation'].unique():
            sub = df_heap[df_heap['operation'] == op].groupby('input_size')['time_ms'].mean().reset_index()
            ax.plot(sub['input_size'], sub['time_ms'], marker='o', label=f'Heap {op}', linewidth=2)
        ax.set_title('PriorityQueue (Min-Heap) Dispatch Benchmark', fontsize=12, fontweight='bold')
        ax.set_xlabel('Number of Fault Requests (n)')
        ax.set_ylabel('Mean Time (ms)')
        ax.legend()
        ax.grid(True, linestyle='--', alpha=0.6)
        plt.tight_layout()
        plt.savefig('reports/graphs/ECG_heap_priority_dispatch_graph.png', dpi=300)
        plt.close()
        print("Generated: reports/graphs/ECG_heap_priority_dispatch_graph.png")

    # 4. Tree Comparison Graph (BST vs Red-Black Tree)
    if os.path.exists('data/tree_experiment.csv'):
        df_tree = pd.read_csv('data/tree_experiment.csv')
        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5))
        for ttype in df_tree['tree_type'].unique():
            sub = df_tree[df_tree['tree_type'] == ttype].groupby('input_size').mean(numeric_only=True).reset_index()
            ax1.plot(sub['input_size'], sub['height'], marker='o', label=f'{ttype} Height', linewidth=2)
            ax2.plot(sub['input_size'], sub['search_ms'], marker='s', label=f'{ttype} Search', linewidth=2)
        ax1.set_title('Tree Height: BST vs Red-Black Tree', fontsize=12, fontweight='bold')
        ax1.set_xlabel('Number of Inserted Nodes (n)')
        ax1.set_ylabel('Tree Height')
        ax1.legend()
        ax1.grid(True, linestyle='--', alpha=0.6)

        ax2.set_title('Tree Search Time: BST vs Red-Black Tree', fontsize=12, fontweight='bold')
        ax2.set_xlabel('Number of Inserted Nodes (n)')
        ax2.set_ylabel('Search Time (ms)')
        ax2.legend()
        ax2.grid(True, linestyle='--', alpha=0.6)

        plt.tight_layout()
        plt.savefig('reports/graphs/ECG_tree_indexing_comparison_graph.png', dpi=300)
        plt.close()
        print("Generated: reports/graphs/ECG_tree_indexing_comparison_graph.png")

    # 5. Graph Algorithm Timing Graph
    if os.path.exists('data/graph_experiment.csv'):
        df_graph = pd.read_csv('data/graph_experiment.csv')
        fig, ax = plt.subplots(figsize=(9, 5))
        for algo in df_graph['algorithm'].unique():
            sub = df_graph[df_graph['algorithm'] == algo].groupby('vertices')['time_ms'].mean().reset_index()
            ax.plot(sub['vertices'], sub['time_ms'], marker='o', label=algo, linewidth=2)
        ax.set_title('Graph Algorithms Timing (BFS, DFS, Dijkstra, Prim, Kruskal)', fontsize=12, fontweight='bold')
        ax.set_xlabel('Number of Grid Vertices (|V|)')
        ax.set_ylabel('Mean Execution Time (ms)')
        ax.legend()
        ax.grid(True, linestyle='--', alpha=0.6)
        plt.tight_layout()
        plt.savefig('reports/graphs/ECG_graph_algorithms_timing_graph.png', dpi=300)
        plt.close()
        print("Generated: reports/graphs/ECG_graph_algorithms_timing_graph.png")

    # 6. Memory Performance Graph
    fig, ax = plt.subplots(figsize=(8, 5))
    sizes = [100, 500, 1000, 5000, 10000]
    mem_linear = [s * 4 / 1024 for s in sizes]
    mem_quadratic = [(s * 8 + 64) / 1024 for s in sizes]
    ax.plot(sizes, mem_linear, marker='o', label='O(n) Memory (Array / List)', color='#1f77b4', linewidth=2)
    ax.plot(sizes, mem_quadratic, marker='s', label='O(V+E) Graph Memory', color='#2ca02c', linewidth=2)
    ax.set_title('Estimated Memory Footprint Across Scale', fontsize=12, fontweight='bold')
    ax.set_xlabel('Input Size (n / V)')
    ax.set_ylabel('Memory Footprint (KB)')
    ax.legend()
    ax.grid(True, linestyle='--', alpha=0.6)
    plt.tight_layout()
    plt.savefig('reports/graphs/ECG_memory_performance_graph.png', dpi=300)
    plt.close()
    print("Generated: reports/graphs/ECG_memory_performance_graph.png")

if __name__ == '__main__':
    run_java_experiments()
    generate_charts()
